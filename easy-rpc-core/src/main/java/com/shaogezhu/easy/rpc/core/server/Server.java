package com.shaogezhu.easy.rpc.core.server;

import com.shaogezhu.easy.rpc.core.common.RpcDecoder;
import com.shaogezhu.easy.rpc.core.common.RpcEncoder;
import com.shaogezhu.easy.rpc.core.common.ServerServiceSemaphoreWrapper;
import com.shaogezhu.easy.rpc.core.common.annotations.SPI;
import com.shaogezhu.easy.rpc.core.common.config.PropertiesBootstrap;
import com.shaogezhu.easy.rpc.core.common.event.RpcListenerLoader;
import com.shaogezhu.easy.rpc.core.common.utils.CommonUtil;
import com.shaogezhu.easy.rpc.core.filter.ServerFilter;
import com.shaogezhu.easy.rpc.core.filter.server.ServerAfterFilterChain;
import com.shaogezhu.easy.rpc.core.filter.server.ServerBeforeFilterChain;
import com.shaogezhu.easy.rpc.core.registy.AbstractRegister;
import com.shaogezhu.easy.rpc.core.registy.RegistryService;
import com.shaogezhu.easy.rpc.core.registy.URL;
import com.shaogezhu.easy.rpc.core.serialize.SerializeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.EXTENSION_LOADER;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.PROVIDER_SERVICE_WRAPPER_MAP;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.PROVIDER_URL_SET;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.REGISTRY_SERVICE;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_AFTER_FILTER_CHAIN;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_BEFORE_FILTER_CHAIN;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_CHANNEL_DISPATCHER;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_CONFIG;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_SERIALIZE_FACTORY;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonServerCache.SERVER_SERVICE_SEMAPHORE_MAP;
import static com.shaogezhu.easy.rpc.core.common.constants.RpcConstants.DEFAULT_DECODE_CHAR;
import static com.shaogezhu.easy.rpc.core.spi.ExtensionLoader.EXTENSION_LOADER_CLASS_CACHE;

/**
 * @Author peng
 * @Date 2023/2/23 22:21
 */
public class Server {

    public void startServerApplication() throws InterruptedException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);
        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.option(ChannelOption.TCP_NODELAY, true);
        bootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        bootstrap.option(ChannelOption.SO_SNDBUF, 16 * 1024)
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                .option(ChannelOption.SO_KEEPALIVE, true);

        //服务端采用单一长连接的模式，这里所支持的最大连接数和机器本身的性能有关
        //连接防护的handler应该绑定在Main-Reactor上
        bootstrap.handler(new MaxConnectionLimitHandler(SERVER_CONFIG.getMaxConnections()));
        bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ByteBuf delimiter = Unpooled.copiedBuffer(DEFAULT_DECODE_CHAR.getBytes());
                ch.pipeline().addLast(new DelimiterBasedFrameDecoder(SERVER_CONFIG.getMaxServerRequestData(), delimiter));
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ServerHandler());
            }
        });

        //初始化监听器
        RpcListenerLoader rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();

        //初始化序列化器
        String serverSerialize = SERVER_CONFIG.getServerSerialize();
        EXTENSION_LOADER.loadExtension(SerializeFactory.class);
        LinkedHashMap<String, Class<?>> serializeMap = EXTENSION_LOADER_CLASS_CACHE.get(SerializeFactory.class.getName());
        Class<?> serializeClass = serializeMap.get(serverSerialize);
        if (serializeClass == null) {
            throw new RuntimeException("no match serializeClass for " + serverSerialize);
        }
        SERVER_SERIALIZE_FACTORY = (SerializeFactory) serializeClass.newInstance();


        //初始化过滤链
        ServerBeforeFilterChain serverBeforeFilterChain = new ServerBeforeFilterChain();
        ServerAfterFilterChain serverAfterFilterChain = new ServerAfterFilterChain();
        EXTENSION_LOADER.loadExtension(ServerFilter.class);
        LinkedHashMap<String, Class<?>> filterChainMap = EXTENSION_LOADER_CLASS_CACHE.get(ServerFilter.class.getName());
        for (Map.Entry<String, Class<?>> filterChainEntry : filterChainMap.entrySet()) {
            String filterChainKey = filterChainEntry.getKey();
            Class<?> filterChainImpl = filterChainEntry.getValue();
            if (filterChainImpl == null) {
                throw new RuntimeException("no match filterChainImpl for " + filterChainKey);
            }
            SPI spi = (SPI) filterChainImpl.getDeclaredAnnotation(SPI.class);
            if (spi != null && "before".equalsIgnoreCase(spi.value())) {
                serverBeforeFilterChain.addServerFilter((ServerFilter) filterChainImpl.newInstance());
            } else if (spi != null && "after".equalsIgnoreCase(spi.value())) {
                serverAfterFilterChain.addServerFilter((ServerFilter) filterChainImpl.newInstance());
            }
        }
        SERVER_BEFORE_FILTER_CHAIN = serverBeforeFilterChain;
        SERVER_AFTER_FILTER_CHAIN = serverAfterFilterChain;

        //初始化请求分发器
        SERVER_CHANNEL_DISPATCHER.init(SERVER_CONFIG.getServerQueueSize(), SERVER_CONFIG.getServerBizThreadNums());
        SERVER_CHANNEL_DISPATCHER.startDataConsume();

        //暴露服务端url
        this.batchExportUrl();
        bootstrap.bind(SERVER_CONFIG.getPort()).sync();
    }

    public void initServerConfig() {
        SERVER_CONFIG = PropertiesBootstrap.loadServerConfigFromLocal();
    }

    /**
     * 将服务端的具体服务都暴露到注册中心
     */
    public void batchExportUrl() {
        Thread task = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                for (URL url : PROVIDER_URL_SET) {
                    REGISTRY_SERVICE.register(url);
                }
            }
        });
        task.start();
    }

    public void registyService(ServiceWrapper serviceWrapper) {
        Object serviceBean = serviceWrapper.getServiceBean();
        if (serviceBean.getClass().getInterfaces().length == 0) {
            throw new RuntimeException("service must had interfaces!");
        }
        Class<?>[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interfaces!");
        }
        if (REGISTRY_SERVICE == null) {
            try {
                EXTENSION_LOADER.loadExtension(RegistryService.class);
                Map<String, Class<?>> registryClassMap = EXTENSION_LOADER_CLASS_CACHE.get(RegistryService.class.getName());
                Class<?> registryClass = registryClassMap.get(SERVER_CONFIG.getRegisterType());
                REGISTRY_SERVICE = (AbstractRegister) registryClass.newInstance();
            } catch (Exception e) {
                throw new RuntimeException("registryServiceType unKnow,error is ", e);
            }
        }
        //默认选择该对象的第一个实现接口
        Class<?> interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(SERVER_CONFIG.getApplicationName());
        url.addParameter("host", CommonUtil.getIpAddress());
        url.addParameter("port", String.valueOf(SERVER_CONFIG.getPort()));
        url.addParameter("group", String.valueOf(serviceWrapper.getGroup()));
        url.addParameter("limit", String.valueOf(serviceWrapper.getLimit()));
        url.addParameter("weight", String.valueOf(serviceWrapper.getWeight()));
        PROVIDER_URL_SET.add(url);
        if (serviceWrapper.getLimit() > 0) {
            SERVER_SERVICE_SEMAPHORE_MAP.put(interfaceClass.getName(), new ServerServiceSemaphoreWrapper(serviceWrapper.getLimit()));
        }
        if (CommonUtil.isNotEmpty(serviceWrapper.getServiceToken())) {
            PROVIDER_SERVICE_WRAPPER_MAP.put(interfaceClass.getName(), serviceWrapper);
        }
    }

}

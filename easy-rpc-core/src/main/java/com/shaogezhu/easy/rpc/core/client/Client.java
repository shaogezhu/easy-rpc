package com.shaogezhu.easy.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.shaogezhu.easy.rpc.core.common.RpcDecoder;
import com.shaogezhu.easy.rpc.core.common.RpcEncoder;
import com.shaogezhu.easy.rpc.core.common.RpcInvocation;
import com.shaogezhu.easy.rpc.core.common.RpcProtocol;
import com.shaogezhu.easy.rpc.core.common.config.ClientConfig;
import com.shaogezhu.easy.rpc.core.common.event.RpcListenerLoader;
import com.shaogezhu.easy.rpc.core.common.utils.CommonUtil;
import com.shaogezhu.easy.rpc.core.proxy.javassist.JavassistProxyFactory;
import com.shaogezhu.easy.rpc.core.proxy.jdk.JDKProxyFactory;
import com.shaogezhu.easy.rpc.core.registy.AbstractRegister;
import com.shaogezhu.easy.rpc.core.registy.URL;
import com.shaogezhu.easy.rpc.core.registy.zookeeper.ZookeeperRegister;
import com.shaogezhu.easy.rpc.core.router.RandomRouterImpl;
import com.shaogezhu.easy.rpc.core.router.RotateRouterImpl;
import com.shaogezhu.easy.rpc.core.serialize.fastjson.FastJsonSerializeFactory;
import com.shaogezhu.easy.rpc.core.serialize.hessian.HessianSerializeFactory;
import com.shaogezhu.easy.rpc.core.serialize.jdk.JdkSerializeFactory;
import com.shaogezhu.easy.rpc.core.serialize.kryo.KryoSerializeFactory;
import com.shaogezhu.easy.rpc.interfaces.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;
import java.util.Map;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.*;
import static com.shaogezhu.easy.rpc.core.common.constants.RpcConstants.*;

/**
 * @Author peng
 * @Date 2023/2/23 22:48
 */
public class Client {

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private final Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference initClientApplication() throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                //初始化管道，包含了编解码器和客户端响应类
                ch.pipeline().addLast(new RpcEncoder());
                ch.pipeline().addLast(new RpcDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });

        //初始化连接器
        ConnectionHandler.setBootstrap(bootstrap);

        //初始化监听器
        RpcListenerLoader rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();

        //初始化序列化器
        String clientSerialize = clientConfig.getClientSerialize();
        switch (clientSerialize) {
            case JDK_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new JdkSerializeFactory();
                break;
            case FAST_JSON_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new FastJsonSerializeFactory();
                break;
            case HESSIAN2_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new HessianSerializeFactory();
                break;
            case KRYO_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new KryoSerializeFactory();
                break;
            default:
                throw new RuntimeException("no match serialize type for" + clientSerialize);
        }

        //初始化代理工厂
        RpcReference rpcReference;
        if (JAVASSIST_PROXY_TYPE.equals(clientConfig.getProxyType())) {
            rpcReference = new RpcReference(new JavassistProxyFactory());
        } else {
            rpcReference = new RpcReference(new JDKProxyFactory());
        }

        return rpcReference;
    }

    public void initClientConfig() {
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setRegisterAddr("localhost:2181");
        clientConfig.setApplicationName("easy-rpc-client");
        clientConfig.setProxyType("JDK");
        clientConfig.setRouterStrategy("random");
        clientConfig.setClientSerialize("kryo");
        this.setClientConfig(clientConfig);
    }

    private void initClientRouterStrategy() {
        String routerStrategy = clientConfig.getRouterStrategy();
        if (RANDOM_ROUTER_TYPE.equals(routerStrategy)) {
            ROUTER = new RandomRouterImpl();
        } else if (ROTATE_ROUTER_TYPE.equals(routerStrategy)) {
            ROUTER = new RotateRouterImpl();
        }
    }

    /**
     * 启动服务之前需要预先订阅对应的dubbo服务
     */
    public void doSubscribeService(Class<?> serviceBean) {
        if (abstractRegister == null) {
            abstractRegister = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtil.getIpAddress());
        Map<String, String> result = abstractRegister.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(),result);
        abstractRegister.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (URL providerUrl : SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerUrl.getServiceName());
            for (String providerIp : providerIps) {
                try {
                    ConnectionHandler.connect(providerUrl.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            URL url = new URL();
            url.setServiceName(providerUrl.getServiceName());
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
            //客户端在此新增一个订阅的功能
            abstractRegister.doAfterSubscribe(url);
        }
    }

    /**
     * 开启发送线程，专门从事将数据包发送给服务端
     */
    private void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob(), "ClientAsyncSendJobThread");
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     */
    class AsyncSendJob implements Runnable {

        public AsyncSendJob() { }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    //进行序列化
                    byte[] serialize = CLIENT_SERIALIZE_FACTORY.serialize(data);
                    //将RpcInvocation封装到RpcProtocol对象中，然后发送给服务端
                    RpcProtocol rpcProtocol = new RpcProtocol(serialize);
                    //获取netty通道
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {

        Client client = new Client();
        //初始化配置文件
        client.initClientConfig();
        //初始化路由策略
        client.initClientRouterStrategy();
        //初始化客户端
        RpcReference rpcReference = client.initClientApplication();

        //订阅服务
        client.doSubscribeService(DataService.class);
        //建立连接
        client.doConnectServer();
        //启动客户端
        client.startClient();
        System.out.println("========== Client start success ==========");

        //生成代理对象
        DataService dataService = rpcReference.get(DataService.class);
        //调用远程方法
        List<String> list = dataService.getList();
        System.out.println(list);

        for (int i = 100; i < 999; ++i){
            Thread.sleep(1000);
            String msg = i+":msg from client.";
            String s = dataService.sendData(msg);
            System.out.println(i+":"+s);
        }
//        dataService.testError();
//        dataService.testErrorV2();
    }

}

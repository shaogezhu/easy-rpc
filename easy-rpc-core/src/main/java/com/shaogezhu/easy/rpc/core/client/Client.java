package com.shaogezhu.easy.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.shaogezhu.easy.rpc.core.common.RpcDecoder;
import com.shaogezhu.easy.rpc.core.common.RpcEncoder;
import com.shaogezhu.easy.rpc.core.common.RpcInvocation;
import com.shaogezhu.easy.rpc.core.common.RpcProtocol;
import com.shaogezhu.easy.rpc.core.common.config.ClientConfig;
import com.shaogezhu.easy.rpc.core.proxy.jdk.JDKProxyFactory;
import com.shaogezhu.easy.rpc.interfaces.DataService;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.List;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @Author peng
 * @Date 2023/2/23 22:48
 */
public class Client {

    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public RpcReference startClientApplication() throws InterruptedException {
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

        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        System.out.println("========== Client start success ==========");
        this.startClient(channelFuture);

        //注入代理工厂
        return new RpcReference(new JDKProxyFactory());
//        return new RpcReference(new JavassistProxyFactory());
    }

    /**
     * 开启发送线程，专门从事将数据包发送给服务端
     */
    private void startClient(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        asyncSendJob.start();
    }

    /**
     * 异步发送信息任务
     */
    class AsyncSendJob implements Runnable {

        private final ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    //阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    //将RpcInvocation封装到RpcProtocol对象中，然后发送给服务端
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    //netty的通道负责发送数据给服务端
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        //初始化客户端
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setServerAddr("localhost");
        clientConfig.setPort(8010);
        Client client = new Client();
        client.setClientConfig(clientConfig);
        RpcReference rpcReference = client.startClientApplication();
        DataService dataService = rpcReference.get(DataService.class);

        //调用远程方法
        List<String> list = dataService.getList();
        System.out.println(list);

        for (int i = 100; i < 999; ++i){
            Thread.sleep(1000);
            String msg = i+":msg from client.";
            dataService.sendData(msg);
        }
//        dataService.testError();
//        dataService.testErrorV2();
    }

}

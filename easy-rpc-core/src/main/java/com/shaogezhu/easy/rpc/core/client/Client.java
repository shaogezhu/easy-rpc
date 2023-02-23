package com.shaogezhu.easy.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author peng
 * @Date 2023/2/23 22:48
 */
public class Client {

    public ChannelFuture startClientApplication() throws InterruptedException {
        NioEventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(clientGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel ch) throws Exception {
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new StringDecoder());
                ch.pipeline().addLast(new ClientHandler());
            }
        });

        ChannelFuture channelFuture = bootstrap.connect("localhost", 8010).sync();
        System.out.println("========== Client start success ==========");
        return channelFuture;
    }

    public static void main(String[] args) throws InterruptedException {
        Client client = new Client();
        ChannelFuture channelFuture = client.startClientApplication();
        for (int i = 100; i < 999; ++i){
            Thread.sleep(1000);
            String msg = i+":msg from client.";
            channelFuture.channel().writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
        }
    }

}

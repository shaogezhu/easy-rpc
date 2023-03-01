package com.shaogezhu.easy.rpc.core.common;

import io.netty.channel.ChannelFuture;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description: 自定义包装类，将netty建立好的ChannelFuture做了一些封装
 */
public class ChannelFutureWrapper {

    private String host;

    private Integer port;

    private ChannelFuture channelFuture;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

}

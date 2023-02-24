package com.shaogezhu.easy.rpc.core.common.config;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description: 客户端配置类
 */
public class ClientConfig {

    private Integer port;

    private String serverAddr;

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}

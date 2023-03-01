package com.shaogezhu.easy.rpc.core.common.config;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description: 服务端配置类
 */
public class ServerConfig {

    private Integer port;

    private String registerAddr;

    private String applicationName;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}

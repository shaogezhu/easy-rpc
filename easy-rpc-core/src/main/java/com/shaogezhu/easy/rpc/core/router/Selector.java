package com.shaogezhu.easy.rpc.core.router;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:
 */
public class Selector {

    /**
     * 服务命名
     * eg: com.shaogezhu.test.DataService
     */
    private String providerServiceName;

    public String getProviderServiceName() {
        return providerServiceName;
    }

    public void setProviderServiceName(String providerServiceName) {
        this.providerServiceName = providerServiceName;
    }

}

package com.shaogezhu.easy.rpc.core.client;

import com.shaogezhu.easy.rpc.core.proxy.ProxyFactory;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description:
 */
public class RpcReference {

    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     */
    public <T> T get(Class<T> tClass) throws Throwable {
        return proxyFactory.getProxy(tClass);
    }
}
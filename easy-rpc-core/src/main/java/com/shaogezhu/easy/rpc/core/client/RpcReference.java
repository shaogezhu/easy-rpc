package com.shaogezhu.easy.rpc.core.client;

import com.shaogezhu.easy.rpc.core.proxy.ProxyFactory;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description: rpc远程调用类
 */
public class RpcReference {

    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    /**
     * 根据接口类型获取代理对象
     */
    public <T> T get(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        return proxyFactory.getProxy(rpcReferenceWrapper);
    }
}
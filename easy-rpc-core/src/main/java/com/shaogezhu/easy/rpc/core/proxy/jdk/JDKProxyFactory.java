package com.shaogezhu.easy.rpc.core.proxy.jdk;

import com.shaogezhu.easy.rpc.core.client.RpcReferenceWrapper;
import com.shaogezhu.easy.rpc.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description:
 */
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(RpcReferenceWrapper<T> rpcReferenceWrapper) throws Throwable {
        return (T) Proxy.newProxyInstance(rpcReferenceWrapper.getAimClass().getClassLoader(), new Class[]{rpcReferenceWrapper.getAimClass()},
                new JDKClientInvocationHandler(rpcReferenceWrapper));
    }

}
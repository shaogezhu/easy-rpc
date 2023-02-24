package com.shaogezhu.easy.rpc.core.proxy.jdk;

import com.shaogezhu.easy.rpc.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description:
 */
public class JDKProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<?> clazz) throws Throwable {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKClientInvocationHandler(clazz));
    }

}
package com.shaogezhu.easy.rpc.core.proxy.javassist;

import com.shaogezhu.easy.rpc.core.proxy.ProxyFactory;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description:
 */
public class JavassistProxyFactory implements ProxyFactory {

    @Override
    public <T> T getProxy(Class<?> clazz) throws Throwable {
        return (T) ProxyGenerator.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                clazz, new JavassistInvocationHandler(clazz));
    }
}

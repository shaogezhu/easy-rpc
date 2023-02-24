package com.shaogezhu.easy.rpc.core.proxy;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: 代理工厂接口
 */
public interface ProxyFactory {


    <T> T getProxy(final Class<?> clazz) throws Throwable;
}

package com.shaogezhu.easy.rpc.core.proxy.jdk;

import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.RESP_MAP;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: 各种代理工厂统一使用这个InvocationHandler
 */
public class JDKClientInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private final Class<?> clazz;

    public JDKClientInvocationHandler(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(clazz.getName());
        //注入uuid，对每一次的请求都做单独区分
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        //将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < 3*1000) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RESP_MAP.remove(rpcInvocation.getUuid());
                return ((RpcInvocation)object).getResponse();
            }
        }
        RESP_MAP.remove(rpcInvocation.getUuid());
        throw new TimeoutException("client wait server's response timeout!");
    }
}

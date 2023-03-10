package com.shaogezhu.easy.rpc.core.proxy.javassist;

import com.shaogezhu.easy.rpc.core.client.RpcReferenceWrapper;
import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.RESP_MAP;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.SEND_QUEUE;
import static com.shaogezhu.easy.rpc.core.common.constants.RpcConstants.DEFAULT_TIMEOUT;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description:
 */
public class JavassistInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private final RpcReferenceWrapper<?> rpcReferenceWrapper;

    public JavassistInvocationHandler(RpcReferenceWrapper<?> rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        SEND_QUEUE.add(rpcInvocation);
        //如果是异步请求，就没有必要再在RESP_MAP中判断是否有响应结果了
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < DEFAULT_TIMEOUT) {
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

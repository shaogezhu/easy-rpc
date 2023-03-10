package com.shaogezhu.easy.rpc.core.proxy.jdk;

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
 * @Date 2023/2/24
 * @description: 各种代理工厂统一使用这个InvocationHandler
 */
public class JDKClientInvocationHandler implements InvocationHandler {

    private final static Object OBJECT = new Object();

    private int timeOut = DEFAULT_TIMEOUT;

    private final RpcReferenceWrapper<?> rpcReferenceWrapper;

    public JDKClientInvocationHandler(RpcReferenceWrapper<?> rpcReferenceWrapper) {
        this.rpcReferenceWrapper = rpcReferenceWrapper;
        timeOut = Integer.parseInt(rpcReferenceWrapper.getTimeOut());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcInvocation rpcInvocation = new RpcInvocation();
        rpcInvocation.setArgs(args);
        rpcInvocation.setTargetMethod(method.getName());
        rpcInvocation.setTargetServiceName(rpcReferenceWrapper.getAimClass().getName());
        rpcInvocation.setAttachments(rpcReferenceWrapper.getAttatchments());
        //注入uuid，对每一次的请求都做单独区分
        rpcInvocation.setUuid(UUID.randomUUID().toString());
        rpcInvocation.setRetry(rpcReferenceWrapper.getRetry());
        RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
        //将请求的参数放入到发送队列中
        SEND_QUEUE.add(rpcInvocation);
        //如果是异步请求，就没有必要再在RESP_MAP中判断是否有响应结果了
        if (rpcReferenceWrapper.isAsync()) {
            return null;
        }
        long beginTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - beginTime < timeOut || rpcInvocation.getRetry() > 0) {
            Object object = RESP_MAP.get(rpcInvocation.getUuid());
            if (object instanceof RpcInvocation) {
                RpcInvocation rpcInvocationResp = (RpcInvocation) object;
                //异常结果+有重试次数=异常重试
                if (rpcInvocationResp.getE() != null && rpcInvocationResp.getRetry() > 0) {
                    //重新请求
                    rpcInvocation.setE(null);
                    rpcInvocation.setResponse(null);
                    rpcInvocation.setRetry(rpcInvocation.getRetry() - 1);
                    RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                    SEND_QUEUE.add(rpcInvocation);
                    beginTime = System.currentTimeMillis();
                } else {
                    RESP_MAP.remove(rpcInvocation.getUuid());
                    return rpcInvocationResp.getResponse();
                }
            }
            //超时重试
            if (System.currentTimeMillis() - beginTime > timeOut) {
                //重新请求
                rpcInvocation.setResponse(null);
                //每次重试之后都会将retry值扣减1
                rpcInvocation.setRetry(rpcInvocation.getRetry() - 1);
                RESP_MAP.put(rpcInvocation.getUuid(), OBJECT);
                SEND_QUEUE.add(rpcInvocation);
                //充值请求开始的时间
                beginTime = System.currentTimeMillis();
            }
        }
        RESP_MAP.remove(rpcInvocation.getUuid());
        throw new TimeoutException("client wait server's response timeout!");
    }
}

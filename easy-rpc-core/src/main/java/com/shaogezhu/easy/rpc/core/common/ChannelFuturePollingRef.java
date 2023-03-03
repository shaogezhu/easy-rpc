package com.shaogezhu.easy.rpc.core.common;

import java.util.concurrent.atomic.AtomicLong;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:
 */
public class ChannelFuturePollingRef {

    private final AtomicLong referenceTimes = new AtomicLong(0);


    public ChannelFutureWrapper getChannelFutureWrapper(String serviceName){
        ChannelFutureWrapper[] arr = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }

}

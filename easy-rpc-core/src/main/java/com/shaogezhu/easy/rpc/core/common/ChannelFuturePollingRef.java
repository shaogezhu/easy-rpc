package com.shaogezhu.easy.rpc.core.common;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:
 */
public class ChannelFuturePollingRef {

    private final AtomicLong referenceTimes = new AtomicLong(0);


    public ChannelFutureWrapper getChannelFutureWrapper(ChannelFutureWrapper[] arr){
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % arr.length);
        return arr[index];
    }

}

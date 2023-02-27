package com.shaogezhu.easy.rpc.core.common.cache;

import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: 公用缓存 存储请求队列等公共信息
 */
public class CommonClientCache {
    /**
     * 发送队列
     */
    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue<>(100);
    public static Map<String,Object> RESP_MAP = new ConcurrentHashMap<>();
}

package com.shaogezhu.easy.rpc.core.common.event.listener;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description: 监听器接口
 */
public interface RpcListener<T> {

    /**
     * 事件回调方法
     *
     * @param o
     */
    void callBack(Object o);

}

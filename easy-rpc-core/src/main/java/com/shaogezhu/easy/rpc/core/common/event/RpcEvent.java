package com.shaogezhu.easy.rpc.core.common.event;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description: 抽象事件,用于装载需要传递的数据信息
 */
public interface RpcEvent {

    /**
     * 获取事件数据
     * @return
     */
    Object getData();

    /**
     * 设置数据
     * @param data
     * @return
     */
    RpcEvent setData(Object data);
}

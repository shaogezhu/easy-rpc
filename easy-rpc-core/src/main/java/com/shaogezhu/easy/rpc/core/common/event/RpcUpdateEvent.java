package com.shaogezhu.easy.rpc.core.common.event;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description: 节点更新事件
 */
public class RpcUpdateEvent implements RpcEvent {

    private Object data;

    public RpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public RpcEvent setData(Object data) {
        this.data = data;
        return this;
    }
}


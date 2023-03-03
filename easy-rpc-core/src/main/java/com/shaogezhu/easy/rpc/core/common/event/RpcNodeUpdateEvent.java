package com.shaogezhu.easy.rpc.core.common.event;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:
 */
public class RpcNodeUpdateEvent implements RpcEvent {

    private Object data;

    public RpcNodeUpdateEvent(Object data) {
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
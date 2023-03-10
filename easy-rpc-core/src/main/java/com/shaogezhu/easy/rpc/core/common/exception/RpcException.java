package com.shaogezhu.easy.rpc.core.common.exception;

import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

/**
 * @Author peng
 * @Date 2023/3/11
 * @description: 自定义RPC异常
 */
public class RpcException extends RuntimeException {

    private RpcInvocation rpcInvocation;

    public RpcException(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

    public RpcInvocation getRpcInvocation() {
        return rpcInvocation;
    }

    public void setRpcInvocation(RpcInvocation rpcInvocation) {
        this.rpcInvocation = rpcInvocation;
    }

}

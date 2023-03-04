package com.shaogezhu.easy.rpc.core.filter;

import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description: 服务端过滤器
 */
public interface ServerFilter extends Filter {

    /**
     * 执行核心过滤逻辑
     *
     * @param rpcInvocation
     */
    void doFilter(RpcInvocation rpcInvocation);
}
package com.shaogezhu.easy.rpc.core.filter;

import com.shaogezhu.easy.rpc.core.common.ChannelFutureWrapper;
import com.shaogezhu.easy.rpc.core.common.RpcInvocation;

import java.util.List;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description: 客户端过滤器
 */
public interface ClientFilter extends Filter {

    /**
     * 执行过滤链
     *
     * @param src
     * @param rpcInvocation
     * @return
     */
    void doFilter(List<ChannelFutureWrapper> src, RpcInvocation rpcInvocation);
}
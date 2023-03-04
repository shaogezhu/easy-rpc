package com.shaogezhu.easy.rpc.core.router;

import com.shaogezhu.easy.rpc.core.common.ChannelFutureWrapper;
import com.shaogezhu.easy.rpc.core.registy.URL;

import java.util.List;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.*;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:  * @Author peng
 */
public class RotateRouterImpl implements Router {


    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        for (int i=0;i<channelFutureWrappers.size();i++) {
            arr[i]=channelFutureWrappers.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(),arr);
    }

    @Override
    public ChannelFutureWrapper select(ChannelFutureWrapper[] channelFutureWrappers) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(channelFutureWrappers);
    }

    @Override
    public void updateWeight(URL url) {

    }
}

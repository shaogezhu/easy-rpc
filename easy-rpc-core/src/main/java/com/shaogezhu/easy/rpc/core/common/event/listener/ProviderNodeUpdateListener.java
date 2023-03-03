package com.shaogezhu.easy.rpc.core.common.event.listener;

import com.shaogezhu.easy.rpc.core.common.ChannelFutureWrapper;
import com.shaogezhu.easy.rpc.core.common.event.RpcNodeUpdateEvent;
import com.shaogezhu.easy.rpc.core.registy.URL;
import com.shaogezhu.easy.rpc.core.common.event.data.ProviderNodeInfo;

import java.util.List;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.ROUTER;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description:
 */
public class ProviderNodeUpdateListener implements RpcListener<RpcNodeUpdateEvent> {

    @Override
    public void callBack(Object t) {
        ProviderNodeInfo providerNodeInfo = ((ProviderNodeInfo) t);
        List<ChannelFutureWrapper> channelFutureWrappers =  CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper : channelFutureWrappers) {
            String address = channelFutureWrapper.getHost()+":"+channelFutureWrapper.getPort();
            if(address.equals(providerNodeInfo.getAddress())){
                //修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                //更新权重
                ROUTER.updateWeight(url);
                break;
            }
        }
    }

}

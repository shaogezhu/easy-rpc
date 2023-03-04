package com.shaogezhu.easy.rpc.core.router;

import com.shaogezhu.easy.rpc.core.common.ChannelFutureWrapper;
import com.shaogezhu.easy.rpc.core.registy.URL;

import java.util.*;

import static com.shaogezhu.easy.rpc.core.common.cache.CommonClientCache.*;

/**
 * @Author peng
 * @Date 2023/3/3
 * @description: 随机筛选
 */
public class RandomRouterImpl implements Router {


    @Override
    public void refreshRouterArr(Selector selector) {
        //获取服务提供者的数目
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrappers.size()];
        //提前生成调用先后顺序的随机数组
        Integer[] result = createRandomIndex(arr.length);
        //生成对应服务集群的每台机器的调用顺序
        for (int i = 0; i < result.length; i++) {
            arr[i] = channelFutureWrappers.get(result[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
        URL url = new URL();
        url.setServiceName(selector.getProviderServiceName());
        //更新权重
        ROUTER.updateWeight(url);
    }

    @Override
    public ChannelFutureWrapper select(ChannelFutureWrapper[] channelFutureWrappers) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(channelFutureWrappers);
    }

    @Override
    public void updateWeight(URL url) {
        //服务节点的权重
        List<ChannelFutureWrapper> channelFutureWrappers = CONNECT_MAP.get(url.getServiceName());
        Integer[] weightArr = createWeightArr(channelFutureWrappers);
        Integer[] finalArr = createRandomArr(weightArr);
        ChannelFutureWrapper[] finalChannelFutureWrappers = new ChannelFutureWrapper[finalArr.length];
        for (int i = 0; i < finalArr.length; i++) {
            finalChannelFutureWrappers[i] = channelFutureWrappers.get(finalArr[i]);
        }
        SERVICE_ROUTER_MAP.put(url.getServiceName(), finalChannelFutureWrappers);
    }

    private static Integer[] createWeightArr(List<ChannelFutureWrapper> channelFutureWrappers) {
        List<Integer> weightArr = new ArrayList<>();
        for (int k = 0; k < channelFutureWrappers.size(); k++) {
            Integer weight = channelFutureWrappers.get(k).getWeight();
            int c = weight / 100;
            for (int i = 0; i < c; i++) {
                weightArr.add(k);
            }
        }
        return weightArr.toArray(new Integer[0]);
    }

    /**
     * 创建随机乱序数组
     */
    private static Integer[] createRandomArr(Integer[] arr) {
        int total = arr.length;
        Random random = new Random();
        for (int i = 0; i < total; i++) {
            int j = random.nextInt(total);
            if (i == j) continue;
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
        return arr;
    }

    private Integer[] createRandomIndex(int len) {
        Random random = new Random();
        ArrayList<Integer> list = new ArrayList<>(len);
        int index = 0;
        while (index < len) {
            int num = random.nextInt(len);
            //如果不包含这个元素则赋值给集合数组
            if (!list.contains(num)) {
                list.add(index++, num);
            }
        }
        return list.toArray(new Integer[0]);
    }

}

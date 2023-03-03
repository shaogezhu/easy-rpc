package com.shaogezhu.easy.rpc.core.common.event;

import com.shaogezhu.easy.rpc.core.common.event.listener.ProviderNodeUpdateListener;
import com.shaogezhu.easy.rpc.core.common.event.listener.RpcListener;
import com.shaogezhu.easy.rpc.core.common.event.listener.ServiceDestroyListener;
import com.shaogezhu.easy.rpc.core.common.event.listener.ServiceUpdateListener;
import com.shaogezhu.easy.rpc.core.common.utils.CommonUtil;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author peng
 * @Date 2023/2/27
 * @description:
 */
public class RpcListenerLoader {

    private static List<RpcListener<?>> rpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(RpcListener<?> rpcListener) {
        rpcListenerList.add(rpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
        registerListener(new ServiceDestroyListener());
        registerListener(new ProviderNodeUpdateListener());
    }

    public static void sendEvent(RpcEvent rpcEvent) {
        if(CommonUtil.isEmptyList(rpcListenerList)){
            return;
        }
        for (RpcListener<?> rpcListener : rpcListenerList) {
            Class<?> type = getInterfaceT(rpcListener);
            if(type != null && type.equals(rpcEvent.getClass())){
                eventThreadPool.execute(()->{
                    try {
                        rpcListener.callBack(rpcEvent.getData());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                });
            }
        }
    }

    /**
     * 同步事件处理，可能会堵塞
     */
    public static void sendSyncEvent(RpcEvent iRpcEvent) {
        System.out.println("rpcListenerList："+rpcListenerList);
        if (CommonUtil.isEmptyList(rpcListenerList)) {
            return;
        }
        for (RpcListener<?> rpcListener : rpcListenerList) {
            Class<?> type = getInterfaceT(rpcListener);
            if (type != null && type.equals(iRpcEvent.getClass())) {
                try {
                    rpcListener.callBack(iRpcEvent.getData());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取接口上的泛型T
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }



}

package com.shaogezhu.easy.rpc.core.common.cache;

import com.shaogezhu.easy.rpc.core.common.ServerServiceSemaphoreWrapper;
import com.shaogezhu.easy.rpc.core.common.config.ServerConfig;
import com.shaogezhu.easy.rpc.core.dispatcher.ServerChannelDispatcher;
import com.shaogezhu.easy.rpc.core.filter.server.ServerAfterFilterChain;
import com.shaogezhu.easy.rpc.core.filter.server.ServerBeforeFilterChain;
import com.shaogezhu.easy.rpc.core.registy.RegistryService;
import com.shaogezhu.easy.rpc.core.registy.URL;
import com.shaogezhu.easy.rpc.core.serialize.SerializeFactory;
import com.shaogezhu.easy.rpc.core.server.ServiceWrapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author peng
 * @Date 2023/2/24
 * @description: 公用缓存 存储提供的服务等公共信息
 */
public class CommonServerCache {
    /**
     * 需要注册的对象统一放在一个MAP集合中进行管理
     */
    public static final Map<String,Object> PROVIDER_CLASS_MAP = new HashMap<>();
    /**
     * 服务提供者提供的URL
     */
    public static final Set<URL> PROVIDER_URL_SET = new HashSet<>();
    /**
     * 注册中心：用于服务端 服务的注册url和下线
     */
    public static RegistryService REGISTRY_SERVICE;
    /**
     * 服务端序列化工厂
     */
    public static SerializeFactory SERVER_SERIALIZE_FACTORY;
    /**
     * 服务端过滤链
     */
    public static ServerBeforeFilterChain SERVER_BEFORE_FILTER_CHAIN;
    public static ServerAfterFilterChain SERVER_AFTER_FILTER_CHAIN;
    /**
     * 服务端配置类
     */
    public static ServerConfig SERVER_CONFIG;
    /**
     * 用于过滤链的Map<ServiceName,服务端包装类>
     */
    public static final Map<String, ServiceWrapper> PROVIDER_SERVICE_WRAPPER_MAP = new ConcurrentHashMap<>();
    /**
     * 请求分发器
     */
    public static ServerChannelDispatcher SERVER_CHANNEL_DISPATCHER = new ServerChannelDispatcher();
    /**
     * 用于服务端限流
     */
    public static final Map<String, ServerServiceSemaphoreWrapper> SERVER_SERVICE_SEMAPHORE_MAP = new ConcurrentHashMap<>(64);
}

package com.shaogezhu.easy.rpc.core.common.cache;

import java.util.HashMap;
import java.util.Map;

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
}

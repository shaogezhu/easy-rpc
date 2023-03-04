package com.shaogezhu.easy.rpc.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.shaogezhu.easy.rpc.core.serialize.SerializeFactory;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description: FastJson序列化工厂
 */
public class FastJsonSerializeFactory implements SerializeFactory {

    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data),clazz);
    }

}

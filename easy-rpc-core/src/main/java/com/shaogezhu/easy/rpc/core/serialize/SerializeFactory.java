package com.shaogezhu.easy.rpc.core.serialize;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description: 序列化的接口
 */
public interface SerializeFactory {


    /**
     * 序列化
     *
     * @param t
     * @param <T>
     * @return
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     *
     * @param data
     * @param clazz
     * @param <T>
     * @return
     */
    <T> T deserialize(byte[] data, Class<T> clazz);
}

package com.shaogezhu.easy.rpc.interfaces;

import java.util.List;

/**
 * @Author peng
 * @Date 2023/2/25
 * @description: 自定义的测试类
 */
public interface DataService {

    /**
     * 发送数据
     */
    String sendData(String body);

    /**
     * 获取数据
     */
    List<String> getList();


    /**
     * 异常测试方法
     */
    void testError();

    /**
     * 异常测试方法
     */
    String testErrorV2();
}

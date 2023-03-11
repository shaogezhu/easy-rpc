package com.shaogezhu.easy.rpc.provider.springboot.service.impl;

import com.shaogezhu.easy.rpc.interfaces.UserService;
import com.shaogezhu.easy.rpc.spring.starter.common.EasyRpcService;

/**
 * @Author peng
 * @Date 2023/3/11
 * @description:
 */
@EasyRpcService
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("UserServiceImpl : test");
    }
}

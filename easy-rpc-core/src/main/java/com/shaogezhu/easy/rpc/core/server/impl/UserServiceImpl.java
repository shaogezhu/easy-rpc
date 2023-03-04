package com.shaogezhu.easy.rpc.core.server.impl;

import com.shaogezhu.easy.rpc.interfaces.UserService;

/**
 * @Author peng
 * @Date 2023/3/4
 * @description:
 */
public class UserServiceImpl implements UserService {

    @Override
    public void test() {
        System.out.println("UserServiceImpl : test");
    }
}

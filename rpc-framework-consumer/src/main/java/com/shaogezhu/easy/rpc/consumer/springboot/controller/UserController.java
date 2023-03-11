package com.shaogezhu.easy.rpc.consumer.springboot.controller;

import com.shaogezhu.easy.rpc.interfaces.DataService;
import com.shaogezhu.easy.rpc.interfaces.UserService;
import com.shaogezhu.easy.rpc.spring.starter.common.EasyRpcReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @Author peng
 * @Date 2023/3/12
 * @description:
 */
@RestController
@RequestMapping(value = "/user")
public class UserController {

    @EasyRpcReference
    private UserService userService;

    /**
     * 验证各类参数配置是否异常
     */
    @EasyRpcReference(group = "data-group", serviceToken = "data-token")
    private DataService dataService;

    @GetMapping(value = "/test")
    public void test(){
        userService.test();
    }


    @GetMapping(value = "/send/{msg}")
    public String testMaxData(@PathVariable(name = "msg") String msg){
        return dataService.sendData(msg);
    }


    @GetMapping(value = "/list")
    public List<String> getOrderNo(){
        return dataService.getList();
    }

}

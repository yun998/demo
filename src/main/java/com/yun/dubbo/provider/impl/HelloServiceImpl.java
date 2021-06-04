package com.yun.dubbo.provider.impl;

import com.yun.dubbo.provider.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello: " + name;
    }
}

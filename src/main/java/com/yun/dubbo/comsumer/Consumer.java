package com.yun.dubbo.comsumer;

import com.yun.dubbo.framework.factory.ProxyFactory;
import com.yun.dubbo.provider.HelloService;

public class Consumer {

    public static void main(String[] args) {

        HelloService helloService = ProxyFactory.getProxy(HelloService.class);
        String sayHello = helloService.sayHello("lol");
        System.out.println(sayHello);
    }
}

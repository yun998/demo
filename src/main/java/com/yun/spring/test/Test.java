package com.yun.spring.test;

import com.yun.spring.YunApplicationContext;
import com.yun.spring.config.ApplicationConfig;

public class Test {
    public static void main(String[] args) {
        YunApplicationContext yun = new YunApplicationContext(ApplicationConfig.class);
        System.out.println(yun.getBean("userService"));
        System.out.println(yun.getBean("userService"));
        System.out.println(yun.getBean("userService"));
        System.out.println(yun.getBean("user"));
        System.out.println(yun.getBean("user"));
        System.out.println(yun.getBean("user"));
    }
}

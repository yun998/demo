package com.yun.spring.test;

import com.yun.spring.YunApplicationContext;
import com.yun.spring.config.ApplicationConfig;
import com.yun.spring.service.UserService;

public class Test {
    public static void main(String[] args) {
        YunApplicationContext yun = new YunApplicationContext(ApplicationConfig.class);
        UserService userService = (UserService)yun.getBean("userServiceImpl");
        userService.test();
    }
}

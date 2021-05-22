package com.yun.spring;

import com.yun.spring.config.ApplicationConfig;

public class Test {

    @org.junit.Test
    public void testYunApplicationContext(){
        YunApplicationContext yun = new YunApplicationContext(ApplicationConfig.class);
    }
}

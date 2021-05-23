package com.yun.spring.service.se;

import com.yun.spring.annotations.Component;
import com.yun.spring.interfaces.BeanPostProcessor;

@Component("user")
public class User implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitializing(Object bean, String name) {
        System.out.println(name + "初始化之前");
        return bean;
    }

    @Override
    public Object postProcessorAfterInitializing(Object bean, String name) {
        System.out.println(name + "初始化之后");
        return bean;
    }
}

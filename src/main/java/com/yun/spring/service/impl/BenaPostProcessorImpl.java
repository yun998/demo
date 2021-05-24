package com.yun.spring.service.impl;

import com.yun.spring.annotations.Component;
import com.yun.spring.interfaces.BeanPostProcessor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 后置处理器
 */
@Component
public class BenaPostProcessorImpl implements BeanPostProcessor {
    @Override
    public Object postProcessorBeforeInitializing(Object bean, String name) {
        if ("userServiceImpl".equals(name)) {
            System.out.println(name + "初始化之前");
        }
        return bean;
    }

    @Override
    public Object postProcessorAfterInitializing(Object bean, String name) {
        if ("userServiceImpl".equals(name)) {
            System.out.println(name + "初始化之后");
            // 动态代理
            Object proxyInstance = Proxy.newProxyInstance(BenaPostProcessorImpl.class.getClassLoader(), bean.getClass().getInterfaces(), new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    return method.invoke(bean, args);
                }
            });
            return proxyInstance;
        }
        return bean;
    }
}

package com.yun.springmvc.handler.impl;

import com.yun.springmvc.handler.HandlerMapping;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 处理spring-mvc.xml中beanid的映射
 */
@Component
public class BeanIdHandlerMapping implements HandlerMapping {
    static Map<String, Object> map = new HashMap<>();
    @Override
    public Object getHandler(String url) {
        return map.get(url);
    }

    /**
     * spring容器初始化后把xml配置的bean放入map
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        if (beanName.startsWith("/")){
            map.put(beanName, bean);
        }
        return false;
    }
}

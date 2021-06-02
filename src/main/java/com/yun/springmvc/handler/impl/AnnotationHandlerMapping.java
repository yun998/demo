package com.yun.springmvc.handler.impl;

import com.yun.springmvc.annotations.RequestMapping;
import com.yun.springmvc.handler.HandlerMapping;
import com.yun.springmvc.pojo.RequestMappingInfo;
import org.springframework.beans.BeansException;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Component
public class AnnotationHandlerMapping implements HandlerMapping {
    static Map<String, RequestMappingInfo> map = new HashMap<>();
    @Override
    public Object getHandler(String url) {
        return map.get(url);
    }

    /**
     * spring容器初始化后把带@RequestMapping方法的描述信息放入map
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName) throws BeansException {
        Method[] methods = bean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            RequestMappingInfo info = createRequestMappingInfo(bean, method);
            map.put(info.getUrl(), info);
        }
        return false;
    }

    /**
     * 把bean方法上有@RequestMapping的对象信息存入RequestMappingInfo
     * @param bean
     * @param method
     * @return
     */
    private RequestMappingInfo createRequestMappingInfo(Object bean, Method method){
        RequestMappingInfo info = new RequestMappingInfo();
        if (method.isAnnotationPresent(RequestMapping.class)) {
            info.setObject(bean);
            info.setMethod(method);
            info.setUrl(method.getDeclaredAnnotation(RequestMapping.class).value());
        }
        return info;
    }
}

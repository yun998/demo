package com.yun.springmvc.handler.impl;

import com.yun.springmvc.annotations.RequestParam;
import com.yun.springmvc.handler.HandlerApdater;
import com.yun.springmvc.pojo.RequestMappingInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public class AnnotationHandlerApdater implements HandlerApdater {
    @Override
    public boolean support(Object handler) {
        return handler instanceof RequestMappingInfo;
    }

    @Override
    public Object handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        // 获取请求携带的参数
        Map<String, String[]> parameterMap = req.getParameterMap();

        RequestMappingInfo requestMappingInfo = (RequestMappingInfo)handler;
        Method method = requestMappingInfo.getMethod();
        Parameter[] parameters = method.getParameters();

        Object[] paramValue = new Object[parameters.length];
        // 对比HttpServletRequest和带@RequestMapping注解方法中的参数
        for (int i = 0; i < parameters.length; i++) {
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String key = entry.getKey();
                if (key != null && key.equals(parameters[i].getAnnotation(RequestParam.class).value())) {
                    paramValue[i] = entry.getValue()[0];
                }
            }
        }
        return method.invoke(requestMappingInfo.getObject(), paramValue);
    }
}

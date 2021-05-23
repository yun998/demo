package com.yun.spring.interfaces;

public interface BeanPostProcessor {
    /**
     * 初始化之前
     * @param name
     * @return
     */
    Object postProcessorBeforeInitializing(Object bean, String name);
    /**
     * 初始化之后
     * @param name
     * @return
     */
    Object postProcessorAfterInitializing(Object bean, String name);
}

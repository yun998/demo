package com.yun.springmvc.handler;

import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;

/**
 * 映射器
 */
public interface HandlerMapping extends InstantiationAwareBeanPostProcessor {
    Object getHandler(String url);
}

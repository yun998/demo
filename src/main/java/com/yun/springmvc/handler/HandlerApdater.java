package com.yun.springmvc.handler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 适配器
 */
public interface HandlerApdater {
    boolean support(Object handler);

    Object handle(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception;
}

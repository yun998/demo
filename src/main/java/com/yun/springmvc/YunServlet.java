package com.yun.springmvc;

import com.yun.springmvc.handler.HandlerApdater;
import com.yun.springmvc.handler.HandlerMapping;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Map;

/**
 *  前端控制器
 *  处理所有的请求逻辑
 */
public class YunServlet extends HttpServlet {

    private String contextConfig;
    // 映射器
    static Collection<HandlerMapping> handlerMappings;
    // 适配器
    static Collection<HandlerApdater> handlerApdaters;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        Object handler = getHandlerMapping(req);
        HandlerApdater apdater = getHandlerApdater(handler);
        Object result = null;

        try {
            result = apdater.handle(req, resp, handler);
        } catch (Exception e) {
            e.printStackTrace();
        }

        PrintWriter writer = resp.getWriter();
        writer.println(result);
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        // 读取配置文件
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(config.getInitParameter("contextConfigLocation"));

        Map<String, HandlerMapping> handlerMappingMap = context.getBeansOfType(HandlerMapping.class);
        handlerMappings = handlerMappingMap.values();

        Map<String, HandlerApdater> handlerApdaterMap = context.getBeansOfType(HandlerApdater.class);
        handlerApdaters = handlerApdaterMap.values();
    }

    private Object getHandlerMapping(HttpServletRequest req){
        if (handlerMappings != null) {
            for (HandlerMapping handlerMapping : handlerMappings) {
                Object handler = handlerMapping.getHandler(req.getRequestURI());
                if(handler != null){
                    return handler;
                }
            }
        }
        return null;
    }

    private HandlerApdater getHandlerApdater(Object handler){
        if (handlerApdaters != null) {
            for (HandlerApdater handlerApdater : handlerApdaters) {
                boolean flag = handlerApdater.support(handler);
                if (flag) {
                    return handlerApdater;
                }
            }
        }
        return null;
    }
}

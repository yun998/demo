package com.yun.dubbo.framework.protocol.http;

import com.alibaba.fastjson.JSONObject;
import com.yun.dubbo.framework.pojo.Invocation;
import com.yun.dubbo.framework.registry.LocalRegister;
import org.apache.commons.io.IOUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HttpServerHandler {
    public void handler(HttpServletRequest req, HttpServletResponse resp){
        // 处理请求的逻辑
        // 解析请求-->调用服务-->1.HelloService 2.sayHello 3.参数类型列表 4.方法参数
        try {
            Invocation invocation = JSONObject.parseObject(req.getInputStream(), Invocation.class);

            String interfaceName = invocation.getInterfaceName();
            String methodName = invocation.getMethodName();
            Class[] paramType = invocation.getParamType();

            // 执行子服务
            Class implClass = LocalRegister.getImplClass(interfaceName);
            Method method = implClass.getMethod(methodName, paramType);
            String result = (String) method.invoke(implClass.newInstance(), invocation.getParams());

            // 返回结果
            IOUtils.write(result, resp.getOutputStream());

        } catch (IOException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }
}

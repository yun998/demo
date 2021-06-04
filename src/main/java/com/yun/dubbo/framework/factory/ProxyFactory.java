package com.yun.dubbo.framework.factory;

import com.yun.dubbo.framework.lb.LoadBalance;
import com.yun.dubbo.framework.pojo.Invocation;
import com.yun.dubbo.framework.pojo.URL;
import com.yun.dubbo.framework.protocol.http.HttpClient;
import com.yun.dubbo.framework.registry.RemoteMapRegister;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class ProxyFactory {

    public static <T> T getProxy(final Class interfaceClass){

        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[]{interfaceClass}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                HttpClient httpClient = new HttpClient();
                Invocation invocation = new Invocation(interfaceClass.getName(), method.getName(), method.getParameterTypes(), args);

                String result = httpClient.send("localhost", 8080, invocation);

                // 配置注册中心
                // List<URL> urls = RemoteMapRegister.getUrlList(interfaceClass.getName());

                // 负载均衡
                // URL url = LoadBalance.random(urls);

                // String result = httpClient.send(url.getHostName(), url.getPort(), invocation);

                return result;
            }
        });
    }
}

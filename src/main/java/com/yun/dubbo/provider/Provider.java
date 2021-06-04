package com.yun.dubbo.provider;

import com.yun.dubbo.framework.pojo.URL;
import com.yun.dubbo.framework.protocol.http.HttpServer;
import com.yun.dubbo.framework.registry.LocalRegister;
import com.yun.dubbo.framework.registry.RemoteMapRegister;
import com.yun.dubbo.provider.impl.HelloServiceImpl;

public class Provider {
    public static void main(String[] args) {

        // 本地注册 暴露服务
        LocalRegister.regist(HelloService.class.getName(), HelloServiceImpl.class);

        // 注册中心
        URL url = new URL("localhost", 8080);
        RemoteMapRegister.regist(HelloService.class.getName(), url);

        // 用户的配置（tomcat netty jetty）
        HttpServer httpServer = new HttpServer();
        httpServer.start("localhost", 8080);
    }
}

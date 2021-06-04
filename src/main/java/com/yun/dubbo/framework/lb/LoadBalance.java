package com.yun.dubbo.framework.lb;

import com.yun.dubbo.framework.pojo.URL;

import java.util.List;
import java.util.Random;

public class LoadBalance {

    // 随机负载均衡
    public static URL random(List<URL> urls){
        Random random = new Random();
        int nextInt = random.nextInt(urls.size());
        return urls.get(nextInt);
    }
}

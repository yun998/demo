package com.yun.dubbo.framework.registry;

import com.yun.dubbo.framework.pojo.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteMapRegister {

    private static Map<String, List<URL>> map = new HashMap<>();

    public static void regist(String interfaceName, URL url){
        List<URL> list = map.get(interfaceName);

        if (list == null) {
            list = new ArrayList<>();
        }

        list.add(url);

        map.put(interfaceName, list);
    }

    public static List<URL> getUrlList(String interfaceName){
        return map.get(interfaceName);
    }

    // 通过redis实现多线程共享map
}

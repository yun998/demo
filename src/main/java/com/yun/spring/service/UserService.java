package com.yun.spring.service;

import com.yun.spring.annotations.Autowired;
import com.yun.spring.annotations.Component;
import com.yun.spring.annotations.Scope;
import com.yun.spring.interfaces.BeanNameAware;
import com.yun.spring.interfaces.InitializingBean;
import com.yun.spring.service.se.User;

@Component("userService")
@Scope("prototype")
public class UserService implements BeanNameAware, InitializingBean {

    @Autowired
    private User user;

    private String beanName;

    public void test(){
        System.out.println(user);
        System.out.println(beanName);
    }

    @Override
    public void setBeanName(String name) {
        beanName = name;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("初始化");
    }
}

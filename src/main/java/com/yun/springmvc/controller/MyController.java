package com.yun.springmvc.controller;

import com.yun.springmvc.annotations.RequestMapping;
import com.yun.springmvc.annotations.RequestParam;
import org.springframework.stereotype.Controller;

@Controller
public class MyController {

    @RequestMapping("/test")
    public String test(@RequestParam("name") String name){
        System.out.println(name);
        return "text";
    }
}

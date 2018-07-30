package com.controller;

import com.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class JsonController {

    //使用SpringMVC，想要返回json，极其简单。
    //直接返回对象就行了，数据转换器默认将其转换为json
    @RequestMapping("/")
    @ResponseBody
    public User page(ModelMap modelMap){
        User user = new User();
        user.setName("AAA");
        user.setId(1);
        return user;
    }
}

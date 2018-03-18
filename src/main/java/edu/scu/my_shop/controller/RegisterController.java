package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.service.RegisterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;

    /**
     * 进行注册
     * @param user
     * @return
     */
    @PostMapping("/register")
    public String register(ModelMap modelMap,User user){

        registerService.register(user);

        //登录成功处理
        modelMap.addAttribute("message","注册成功");

        return "/signup";
    }

    /**
     * 注册界面
     * @return
     */
    @GetMapping("/registerpage")
    public String registerpage(){


        return "signup";
    }
}

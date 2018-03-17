package edu.scu.my_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class LoginController {



    @GetMapping("/index")
    @ResponseBody
    public String login(){

        return "主页";
    }

    @PostMapping("/fail")
    @ResponseBody
    public String fail(){
        return "登录失败";
    }


    @GetMapping("/loginpage")
    public String loginpage(){
        System.out.println("true================");
        return "login.html";
    }


}

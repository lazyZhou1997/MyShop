package edu.scu.my_shop.controller;

import org.springframework.web.bind.annotation.*;

@RestController
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


}

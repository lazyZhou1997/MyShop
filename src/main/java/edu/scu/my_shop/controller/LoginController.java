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

    /**
     * 登录失败界面
     * @return
     */
    @PostMapping("/fail")
    @ResponseBody
    public String fail(){
        return "登录失败";
    }


    /**
     * 登录界面，访问登录界面
     * @return
     */
    @GetMapping("/loginpage")
    public String loginpage(){

        return "login";
    }


}

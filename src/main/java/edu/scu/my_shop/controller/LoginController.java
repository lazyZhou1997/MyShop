package edu.scu.my_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {


    @GetMapping("/index")
    @ResponseBody
    public String login(){

        return "主页";
    }


    /**
     * 登录界面，访问登录界面
     * @return
     */
    @GetMapping("/loginpage")
    public String loginpage(){

        return "login";
    }

    /**
     * 登录失败界面
     * @param modelMap
     * @return
     */
    @GetMapping("/fail")
    public String loginFail(ModelMap modelMap){

        //登录成功处理
        modelMap.addAttribute("error","登录失败");

        return "login";
    }


}

package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.result.Result;
import edu.scu.my_shop.service.ChangeUserInfoService;
import edu.scu.my_shop.service.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 改变用户信息
 */
@Controller
public class ChangeUserInfoController {

    @Autowired
    private ChangeUserInfoService changeUserInfoService;

    @Autowired
    private FileService fileService;
    /**
     * 跳转到用户信息页面
     * @return
     */
    @GetMapping("/userInfoPage")
    public String userInfoPage(){

        return "account";
    }

    /**
     * 修改用户信息
     * @param user
     * @return
     */
    @PostMapping("/changeUserInfo")
    public String changeUserInfo(User user){

        //判断输入的用户信息是否合法
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //设置用户userID
        user.setUserId(userDetails.getUserId());

        changeUserInfoService.changeUserInfo(user);

        return "account";
    }

    /**
     * 获取用户信息
     * @return
     */
    @ResponseBody
    @GetMapping("/getUserInfo")
    public Result getUserInfo(){

        //获取当前用户信息
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        //当前用户
        User user = new User();
        user.setUserId(userDetails.getUserId());
        user.setHeadImg(fileService.getUserImageURL(userDetails.getUserId()));
        user.setUserName(userDetails.getUserName());
        user.setBirthday(userDetails.getBirthday());

        //封装当前用户
        Result result = new Result();
        result.setCode(200);
        result.setData(user);

        return result;
    }
}

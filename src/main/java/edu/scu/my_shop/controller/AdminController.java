package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.service.ManageUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/23.
 */
@Controller
public class AdminController {
    @RequestMapping("chuizi")
    public String admin() {
        return "admin";
    }

    @RequestMapping("user/toList.html")
    public String userToList() {
        return "user/toList";
    }

    @RequestMapping("user/toEdit.html")
    public String userToEdit() {
        return "user/toEdit";
    }

    @RequestMapping("product/toList.html")
    public String productToList() {
        return "product/toList";
    }

    @RequestMapping("product/toEdit.html")
    public String productToEdit() {
        return "product/toEdit";
    }

    @RequestMapping("order/toList.html")
    public String orderToList() {
        return "order/toList";
    }

    @RequestMapping("classification/toList.html")
    public String classificationToList() {
        return "classification/toList";
    }

    @RequestMapping("classification/toEdit.html")
    public String classificationToEdit() {
        return "classification/toEdit";
    }

    @Autowired
    private ManageUserService manageUserService;

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<User> getAllUser() {
        List<User> userList = manageUserService.getAllUser();
        return userList;
    }
}

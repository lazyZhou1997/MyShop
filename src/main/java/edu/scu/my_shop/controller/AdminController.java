package edu.scu.my_shop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

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
}

package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.*;
import edu.scu.my_shop.service.CategoryService;
import edu.scu.my_shop.service.ManageUserService;
import edu.scu.my_shop.service.OrderService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/23.
 */
@Controller
public class AdminController {
    @Autowired
    private ManageUserService manageUserService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

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
    public String productToEdit(ModelMap map, String productID) {
        Product product = productService.searchProductByID(productID);
        map.addAttribute("product", product);
        return "product/toEdit";
    }

    @RequestMapping("product/toAdd.html")
    public String productToAdd() {
        return "product/toAdd";
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

    @RequestMapping("getAllUser")
    @ResponseBody
    public List<User> getAllUser() {
        List<User> userList = manageUserService.getAllUser();
        return userList;
    }

    @RequestMapping("getAllProduct")
    @ResponseBody
    public List<Product> getAllProduct() {
        List<Product> productList = productService.getAllProducts();
        return productList;
    }

    @RequestMapping("getAllOrder")
    @ResponseBody
    public List<Order> getAllOrder() {
        List<Order> orderList = orderService.getAllOrder();
        return orderList;
    }

    @RequestMapping("setAdmin")
    @ResponseBody
    public String setAdmin(String userID) {
        manageUserService.appointSuperUser(userID);
        return "";
    }

    @RequestMapping("deleteUser")
    @ResponseBody
    public String deleteUser(String userID) {
        manageUserService.deleteUser(userID);
        return "";
    }

    @RequestMapping("getAllCategory")
    @ResponseBody
    public List<SecondCategory> getAllCategory() {
        List<SecondCategory> secondCategoryList = categoryService.getAllSecondCategory();
        return secondCategoryList;
    }
}

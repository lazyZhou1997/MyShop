package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/21.
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @RequestMapping("cart.html")
    public String cart() {
        return "cart";
    }

    @RequestMapping("productInCart")
    @ResponseBody
    public List<Product> productInCart() {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(); user.setUserId(userDetails.getUserId());
        List<Product> productList = cartService.getAllProducts(user);
        return productList;
    }

    @PostMapping("modifyProductNumber")
    @ResponseBody
    public String modifyProductNumber(@RequestParam("productID")List<String> productIDList, @RequestParam("productNumber")List<Integer> productNumberList) {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(); user.setUserId(userDetails.getUserId());
        cartService.updateProducts(user, productIDList, productNumberList);
        return "";
    }

    @GetMapping("deleteProduct")
    public String deleteProduct(@RequestParam("productID")List<String> productIDList) {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User(); user.setUserId(userDetails.getUserId());
        cartService.deleteProducts(user, productIDList);
        return "cart";
    }
}

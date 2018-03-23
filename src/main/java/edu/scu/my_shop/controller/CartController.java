package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.entity.User;
import edu.scu.my_shop.service.CartService;
import edu.scu.my_shop.service.OrderService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/21.
 */
@Controller
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    @RequestMapping("cart")
    public String cart() {
        return "cart";
    }

    @RequestMapping("cart.html")
    public String cartHTML() {
        return "cart";
    }

    @RequestMapping("payment.html")
    public String payment() {
        return "payment";
    }

    @RequestMapping("productInCart")
    @ResponseBody
    public List<Product> productInCart() {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(userDetails.getUserId());
        List<Product> productList = cartService.getAllProducts(user);
        return productList;
    }

    @PostMapping("modifyProductNumber")
    @ResponseBody
    public String modifyProductNumber(@RequestParam("productID") List<String> productIDList, @RequestParam("productNumber") List<Integer> productNumberList) {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(userDetails.getUserId());
        cartService.updateProducts(user, productIDList, productNumberList);
        return "";
    }

    @GetMapping("deleteProduct")
    public String deleteProduct(@RequestParam("productID") List<String> productIDList) {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = new User();
        user.setUserId(userDetails.getUserId());
        cartService.deleteProducts(user, productIDList);
        return "redirect:cart.html";
    }

    @PostMapping("settleAccount")
    public ModelAndView createOrder(@RequestParam(value = "productID", required = false) List<String> productIDList,
                                    @RequestParam(value = "quantity", required = false) List<Integer> productNumberList) {
        ModelAndView mav = new ModelAndView();

        if (productIDList == null || productIDList.isEmpty() || productNumberList == null || productNumberList.isEmpty()) {
            mav.setViewName("cart.html");
            mav.getModelMap().addAttribute("error", "未选中任何商品");
        } else if (productIDList.size() != productNumberList.size()) {
            mav.setViewName("cart.html");
            mav.getModelMap().addAttribute("error", "信息错误");
        } else {
            // check if product exists
            List<Product> productList = new ArrayList<>();
            for (String productID : productIDList) {
                Product product = productService.searchProductByID(productID);
                if (product == null) {
                    mav.setViewName("cart.html");
                    mav.getModelMap().addAttribute("error", "信息错误");
                    return mav;
                }
                productList.add(product);
            }

            mav.getModelMap().addAttribute("product", productList);
            mav.getModelMap().addAttribute("productNumber", productNumberList);
            mav.setViewName("payment.html");
        }

        return mav;
    }

    /**
     * 传入商品Id和数量，将商品添加到购物车
     * @param productId
     * @param quantity
     */
    @PostMapping("/addProductToCart")
    public void addProductToCart(String productId, int quantity) {

        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User user = new User();
        user.setUserId(userDetails.getUserId());

        //要添加的商品列表
        List<String> products = new ArrayList<>();
        products.add(productId);

        //要添加的商品数量列表
        List<Integer> quantities = new ArrayList<>();
        quantities.add(quantity);

        cartService.insertProducts(user,products,quantities);

        return;
    }
}

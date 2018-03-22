package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/searchProductByName")
    @ResponseBody
    public List<Product> searchProductByName(@RequestParam("productName") String productName){

        return productService.searchProductByName(productName);
    }

    @GetMapping("/search-results.html")
    public String searchResultPage(){

        return "search-results";
    }
}

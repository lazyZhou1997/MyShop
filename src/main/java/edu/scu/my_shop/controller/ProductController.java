package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.result.PageResult;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/searchProductByName")
    public PageResult<Product> searchProductByName(String productName, int pageNum, int pageSize){

        return productService.searchProductByName(productName,pageNum,pageSize);
    }
}

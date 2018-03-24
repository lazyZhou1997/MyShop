package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.service.FileService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @PostMapping("/searchProductByName")
    @ResponseBody
    public List<Product> searchProductByName(@RequestParam("productName") String productName){

        return productService.searchProductByName(productName);
    }

    @GetMapping("/search-results.html")
    public String searchResultPage(){

        return "search-results";
    }

    // TODO: add admin authority
    @GetMapping("getAllProducts")
    public List<Product> getAllProducts() {
        List<Product> productList = productService.getAllProducts();
        return productList;
    }

    /**
     * 添加商品
     * @param product
     * @return
     */
    @PostMapping("addProduct")
    public String insertProduct(Product product) {
        productService.insertProduct(product);
        return "product/toList.html";
    }

    /**
     * 更新商品
     * @param product
     * @return
     */
    @PostMapping("updateProductInfo")
    public String updateProduct(Product product) {
        if (product == null || product.getProductId() == null ) {
            // TODO: return to page or return string
            return "cart.html";
        }
        productService.updateProduct(product);
        return "redirect:product/toEdit.html?productID="+product.getProductId();
    }


    /**
     * 删除商品
     * @param productID
     * @return
     */
    @PostMapping("adminDeleteProduct")
    @ResponseBody
    public String deleteProduct(@RequestParam("productID")String productID) {
        if (productID == null) {
            // TODO: return to page or return string
            return "未知商品";
        }
        fileService.deleteAllProductImages(productID);
        productService.deleteProduct(productID);
        return "";
    }

}

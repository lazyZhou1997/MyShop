package edu.scu.my_shop.controller;

import com.alibaba.fastjson.JSON;
import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.service.FileService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

@Controller
public class ShowProductController {


    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @RequestMapping("/productdetails")
    public String product_details(@RequestParam("productID") String productID, Model model){

        Product product=productService.searchProductByID(productID);
        List<String> productImages=fileService.getProductImagesURL(productID);

        System.out.println(productImages.size());
        for (int i=0;i<productImages.size();i++){
            //  System.out.println(productImages.get(i));
            String[] tmp=productImages.get(i).split("https");
            productImages.set(i,"https"+tmp[1]);
            System.out.println(productImages.get(i));
        }

        Map<String,String> des=(Map) JSON.parse(product.getProductDescription());
        model.addAttribute("des", des);


        // if(product!=null){
        model.addAttribute("product", product);

        model.addAttribute("productImages", productImages);
        return "productdetails";
        //   }
        // return null;
    }
}

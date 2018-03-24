package edu.scu.my_shop.controller;

import com.alibaba.fastjson.JSON;
import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.ProductWithImage;
import edu.scu.my_shop.service.FileService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
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

        List<Product> tuijian = productService.searchProductByCategory("WLjkLuSIrT4UQ5hZL8uKoUoylVZV9m9w5jgMxitVOwwUL3rT2g");


       // List<String> images=new ArrayList<>();

        List<ProductWithImage> pwithImages=new ArrayList<>();

        for (int i=0;i<6;i++){
            List<String> imagesURL=fileService.getProductImagesURL(tuijian.get(i).getProductId());
          //  images.add(imagesURL.get(0));
            ProductWithImage tmp=new ProductWithImage();
            tmp.setComputer(tuijian.get(i));
            if (imagesURL.size()!=0)
            {
                tmp.setComputerImage(imagesURL.get(0));
            }else{
                tmp.setComputerImage("images/home/shipping.jpg");
            }


            pwithImages.add(tmp);
        }

        Map<String,String> des=(Map) JSON.parse(product.getProductDescription());
        model.addAttribute("des", des);


        // if(product!=null){
        model.addAttribute("product", product);

        List<List<String>> my_product_images = new ArrayList<>();


        int productImages_size = productImages.size();

        for(int i=1;i<=productImages_size-productImages_size%3;i=i+3) {
            List<String> my_product_image = new ArrayList<>(3);
            my_product_image.add(productImages.get(i-1));
            my_product_image.add(productImages.get(i));
            my_product_image.add(productImages.get(i+1));
            my_product_images.add(my_product_image);
        }
        List<String> my_product_image = new ArrayList<>();

        for(int i=productImages_size;i>productImages_size-productImages_size%3;i--){

            my_product_image.add(productImages.get(i-1));
        }

        my_product_images.add(my_product_image);

        model.addAttribute("productImages", my_product_images);

        model.addAttribute("tuijian",pwithImages);
        return "productdetails";
        //   }
        // return null;
    }
}

package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.ProductWithImage;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.service.FileService;
import edu.scu.my_shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    /**
     * 登录界面，访问登录界面
     *
     * @return
     */
    @GetMapping("/loginpage")
    public String loginpage() {

        return "login";
    }

    /**
     * 登录失败界面
     *
     * @param modelMap
     * @return
     */
    @GetMapping("/fail")
    public String loginFail(ModelMap modelMap) {

        //登录成功处理
        modelMap.addAttribute("error", "登录失败");

        return "login";
    }


    @RequestMapping(value = {"/index","/"})
    public String indexShowProduct(Model model) {

        //获取用户信息
        SecurityUser userDetail = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //如果是管理员，则跳转到管理员界面
        if (userDetail.getRole()){
            return "admin";
        }


        String[] ID = {"Av7yd1bCsKdy0Tx66j6kmpEHEhjTdchmn7QJJmNt9HYnKS3EDO", "QKZDcx97eVbAMGocGBlNn2wv9cMJcgFrxQlfPMKONVn5ad5Kb9", "qz0tG4n27DTunWh8R2yx6C86iEQy4tc1m1vWC8tAUK7mPyfMoe", "SOXPyLwLdOlP9IhU2nJusILZSZggGi2Zk3sicMxLUHMiPOKaK1", "WLjkLuSIrT4UQ5hZL8uKoUoylVZV9m9w5jgMxitVOwwUL3rT2g"};
        List<List<Product>> computers = productService.searchProductByCategorys(ID, 0, 4);


        List<ProductWithImage> computerswithimages = getProductWithImage(computers.get(0));
        List<ProductWithImage> manwithimages = getProductWithImage(computers.get(1));
        List<ProductWithImage> fruitwithimages = getProductWithImage(computers.get(2));
        List<ProductWithImage> phonewithimages = getProductWithImage(computers.get(3));
        List<ProductWithImage> cametaswithimages = getProductWithImage(computers.get(4));


        model.addAttribute("computerswithimages", computerswithimages);
        model.addAttribute("manwithimages", manwithimages);
        model.addAttribute("fruitwithimages", fruitwithimages);
        model.addAttribute("phonewithimages", phonewithimages);
        model.addAttribute("cametaswithimages", cametaswithimages);

        return "index";
    }

    /**
     * 实现数据的封装
     *
     * @param computers
     * @return
     */
    private List<ProductWithImage> getProductWithImage(List<Product> computers) {

        List<ProductWithImage> computerswithimages = new ArrayList<>();
        List<String> computerImages = getCategoryImages(computers);
        for (int i = 0; i < computers.size(); i++) {
            ProductWithImage computerswithimage = new ProductWithImage();
            computerswithimage.setComputer(computers.get(i));
            computerswithimage.setComputerImage(computerImages.get(i));
            computerswithimages.add(computerswithimage);


        }

        return computerswithimages;
    }


    private List<String> getCategoryImages(List<Product> computers) {
        /**
         * 获取对应电脑商品的
         */
        List<String> computerImages = new ArrayList<>();
        for (Product comupter :
                computers) {

            List<String> productImages = fileService.getProductImagesURL(comupter.getProductId());
            if (productImages.size() == 0) {
                computerImages.add("images/home/gallery1.jpg");
            } else {
            //    String[] tmp = productImages.get(0).split("https");
                computerImages.add(productImages.get(0));
            }

        }

        return computerImages;
    }
}

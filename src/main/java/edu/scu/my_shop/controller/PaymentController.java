package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Address;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/22.
 */
@Controller
public class PaymentController {
    @Autowired
    private AddressService addressService;

    @RequestMapping("getUserAddress")
    @ResponseBody
    public List<Address> getUserAddress() {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Address> addressList = addressService.getAllAddresses(userDetails.getUserId());
        return addressList;
    }
}

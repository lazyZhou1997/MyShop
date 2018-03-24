package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Address;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * Created by Vicent_Chen on 2018/3/22.
 */
@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/addUserAddress")
    public String addUserAddress(Address address, String defaultAddress, ModelMap modelMap) {

        System.out.println(defaultAddress);
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        address.setUserId(userDetails.getUserId());
        address.setIsDefaultAddress(false);//设置为不是默认地址

        //插入数据库，并且获取地址id
        String addressId = addressService.insertAddress(address);

        address.setAddressId(addressId);

        //设置为是默认地址
        if (null != defaultAddress) {

            System.out.println("reach!");
            address.setIsDefaultAddress(true);
            addressService.updateAddress(address);
        }

        modelMap.addAttribute("success", "保存成功");

        return "account";
    }

    @PostMapping("setAddressDefault")
    public String setAddressDefault(@RequestParam(value = "addressID", required = false) String addressID) {

        if (addressID == null || addressID.equals("")) {
            return "未知地址";
        }

        if (!addressService.addressExists(addressID)) {
            return "未知地址";
        }

        Address address = new Address();
        address.setAddressId(addressID);
        address.setIsDefaultAddress(true);

        addressService.updateAddress(address);

        return "";
    }

    @RequestMapping("getUserAddress")
    @ResponseBody
    public List<Address> getUserAddress() {
        SecurityUser userDetails = (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Address> addressList = addressService.getAllAddresses(userDetails.getUserId());
        return addressList;
    }
}

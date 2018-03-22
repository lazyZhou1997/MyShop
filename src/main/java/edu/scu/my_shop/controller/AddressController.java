package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Address;
import edu.scu.my_shop.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Vicent_Chen on 2018/3/22.
 */
@Controller
public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("setAddressDefault")
    public String setAddressDefault(@RequestParam(value = "addressID", required = false)String addressID) {

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
}

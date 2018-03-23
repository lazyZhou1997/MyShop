package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Order;
import edu.scu.my_shop.entity.OrderItem;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static edu.scu.my_shop.service.OrderService.*;

/**
 * Created by Vicent_Chen on 2018/3/23.
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping("submitOrder")
    @ResponseBody
    public String submitOrder(@RequestParam("productID[]")String[] productIDList, @RequestParam("addressID")String addressID) {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userID = userDetails.getUserId();
        orderService.createOrderByProductId(userID, productIDList, addressID);
        return "";
    }

    @RequestMapping("getUserOrders")
    @ResponseBody
    public List<Order> getUserOrders() {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userID = userDetails.getUserId();
        List<Order> orderList = orderService.searchAllOrderByUserId(userID);
        return orderList;
    }

    @RequestMapping("getOrderItems")
    @ResponseBody
    public List<OrderItem> getOrderItems(@RequestParam("orderID") String orderID) {
        List<OrderItem> orderItemList = orderService.getOrderItemByOrderId(orderID);
        return orderItemList;
    }
}

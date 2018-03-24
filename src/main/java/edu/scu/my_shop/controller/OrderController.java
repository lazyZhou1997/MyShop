package edu.scu.my_shop.controller;

import edu.scu.my_shop.entity.Order;
import edu.scu.my_shop.entity.OrderItem;
import edu.scu.my_shop.entity.Product;
import edu.scu.my_shop.entity.SecurityUser;
import edu.scu.my_shop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
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

    /**
     * 获取用户的所有订单
     * @return
     */
    @RequestMapping("getUserOrders")
    @ResponseBody
    public List<Order> getUserOrders() {
        SecurityUser userDetails =  (SecurityUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userID = userDetails.getUserId();
        List<Order> orderList = orderService.searchAllOrderByUserId(userID);
        return orderList;
    }

    /**
     * 传入订单Id获取订单包含的商品
     * @param orderID
     * @return
     */
    @PostMapping("getOrderItems")
    @ResponseBody
    public List<Product> getOrderItems(@RequestParam("orderID") String orderID) {
        return orderService.getOrderItemByOrderId(orderID);
    }

    /**
     * 传入orderId将订单变为已支付状态
     * @param orderId
     * @return
     */
    @PostMapping("payOrderByOrderId")
    @ResponseBody
    public String payOrderByOrderId(String orderId){
        orderService.payOrderByOrderId(orderId);

        return "";
    }
}

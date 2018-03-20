package edu.scu.my_shop.service;

import edu.scu.my_shop.entity.Order;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * OrderService Tester.
 *
 * @author <Authors name>
 * @version 1.0
 * @since <pre>三月 19, 2018</pre>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderServiceTest {

    @Autowired
    private OrderService orderService;

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * 测试生成订单，测试痛过
     * Method: createOrderByProductId(String userId, String[] productIds, String addressId)
     */
    @Test
    public void testCreateOrderByProductId() throws Exception {

        orderService.createOrderByProductId("zzzz", new String[]{"245af984-6484-4dac-9549-beab145471fe"}, "zzzz");

    }


    /**
     * 测试用户取消自己的订单，测试通过
     *
     * @throws Exception
     */
    @Test
    public void testCancelOrderByOrderIdandUserId() throws Exception {
        orderService.cancelOrderByOrderIdandUserId("zzzz", "cfb58774-4e4d-40f4-9e1e-e46b4bfe1c2b");
    }

    /**
     * 测试根据userId和订单状态查找订单，测试通过
     *
     * @throws Exception
     */
    @Test
    public void testSearchOrderByUserIdAndOrderStatus() throws Exception {

        List<Order> orders = orderService.searchAllOrderByUserIdAndOrderStatus("zzz", new String[]{"已付款"});
        for (Order order:
             orders) {

            System.out.println(order.getOrderId());
        }
    }

    /**
     * 测试管理员接收用户订单，测试通过
     *
     * @throws Exception
     */
    @Test
    public void testAcceptOrderByOrderId() throws Exception {

        orderService.acceptOrderByOrderId("cfb58774-4e4d-40f4-9e1e-e46b4bfe1c2b");
    }

} 

package edu.scu.my_shop.service;

import edu.scu.my_shop.dao.CartMapper;
import edu.scu.my_shop.dao.OrderItemMapper;
import edu.scu.my_shop.dao.OrderMapper;
import edu.scu.my_shop.dao.ProductMapper;
import edu.scu.my_shop.entity.*;
import edu.scu.my_shop.exception.OrderServiceException;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static edu.scu.my_shop.exception.OrderServiceException.*;

@Service
public class OrderService {

    public static Integer STATUS_COUNT = 5;

    public static String ORDER_STATUS_NO_PAYMENT = "待付款";
    public static String ORDER_STATUS_HAS_PAYMENT = "已付款";
    public static String ORDER_STATUS_ON_WAY = "正在运送";
    public static String ORDER_STATUS_CANCELED = "已取消";
    public static String ORDER_STATUS_FINISH = "已完成";
    @Autowired
    private SqlSessionFactory sqlSessionFactory;

    @Autowired
    private ProductService productService;

    /**
     * 将选中的商品放入订单中，并对购物车和库存进行相应操作
     *
     * @param userId
     * @param productIds
     */
    @Transactional
    public void createOrderByProductId(String userId, String[] productIds, String addressId) {

        //检查输入
        if (null == userId || null == productIds || null == addressId) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        //已经选中的商品id
        List<String> listProductsId = Arrays.asList(productIds);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        CartMapper cartMapper = sqlSession.getMapper(CartMapper.class);

        //获取选中的购物车中的商品信息
        CartExample cartExample = new CartExample();
        //构建条件
        cartExample.createCriteria().andUserIdEqualTo(userId)
                .andProductIdIn(listProductsId);
        List<Cart> carts = cartMapper.selectByExample(cartExample);

        ProductMapper productMapper = sqlSession.getMapper(ProductMapper.class);
        //更新数据库中商品的数量
        Product product;
        for (Cart cart :
                carts) {

            //查找商品表中对应的商品
            product = productMapper.selectByPrimaryKey(cart.getProductId());
            //判断是否还有库存
            if (null == product || product.getProductLeftTotals() < cart.getTotals()) {
                //抛出库存不足异常
                throw new OrderServiceException(OrderServiceException.NO_ENOUGH_PRODUCT_MESSAGE, OrderServiceException.NO_ENOUGH_PRODUCT);
            }
            //更新商品数量
            product.setProductLeftTotals(product.getProductLeftTotals() - cart.getTotals());
            productMapper.updateByPrimaryKeySelective(product);

        }

        //生成订单并存入数据库
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        Order order = new Order();
        order.setAddressId(addressId);
        order.setOrderDate(new Date());
        String orderId = UUID.randomUUID().toString();
        order.setOrderId(orderId);//订单Id，由UUID生成
        order.setOrderStatus(ORDER_STATUS_NO_PAYMENT);//支付状态，默认待付款
        order.setUserId(userId);
        orderMapper.insert(order);

        //存入订单商品
        OrderItem orderItem;
        OrderItemMapper orderItemMapper = sqlSession.getMapper(OrderItemMapper.class);
        for (Cart cart :
                carts) {

            orderItem = new OrderItem();
            orderItem.setProductCount(cart.getTotals());
            orderItem.setOrderId(orderId);
            orderItem.setProductId(cart.getProductId());

            orderItemMapper.insert(orderItem);
        }

        //删除购物车中已经选中的商品
        cartMapper.deleteByExample(cartExample);

        sqlSession.close();
    }


    /**
     * 普通用户取消自己的订单，如果订单已经运送、取消或者完成，则不能取消
     *
     * @param userId
     * @param orderId
     */
    @Transactional
    public void cancelOrderByOrderIdandUserId(String userId, String orderId) {

        //判断数据的合法性
        if (null == userId || null == orderId) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);
        //查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);

        //判断订单是否可以取消，在运送中或者已经取消、完成的订单不能取消
        if (ORDER_STATUS_CANCELED.equals(order.getOrderStatus()) ||
                ORDER_STATUS_ON_WAY.equals(order.getOrderStatus()) ||
                ORDER_STATUS_FINISH.equals(order.getOrderStatus())) {
            throw new OrderServiceException(OrderServiceException.ORDER_CANNT_CANCEL_MESSAGE, OrderServiceException.ORDER_CANNT_CANCEL);
        }

        //如果userId不一致，则订单不可以取消，因为没有权限
        if (!userId.equals(order.getUserId())) {
            throw new OrderServiceException(OrderServiceException.NO_AUTHORITY_MESSAGE, OrderServiceException.NO_AUTHORITY);
        }

        //如果已经付款
        if (ORDER_STATUS_HAS_PAYMENT.equals(order.getOrderStatus())) {
            //FIXME:退款
            System.out.println("退款");
        }

        //设置订单状态为取消
        order.setOrderStatus(ORDER_STATUS_CANCELED);
        orderMapper.updateByPrimaryKeySelective(order);

        sqlSession.close();

        return;
    }


    /**
     * 根据用户id查找对应状态的订单
     *
     * @param userId
     * @param statuses
     * @return
     */
    public List<Order> searchAllOrderByUserIdAndOrderStatus(String userId, String[] statuses) {

        //检查输入
        if (null == userId || null == statuses) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        List<String> arrayStatuses = Arrays.asList(statuses);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        //查找对应的订单
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUserIdEqualTo(userId).andOrderStatusIn(arrayStatuses);
        List<Order> orders = orderMapper.selectByExample(orderExample);

        //判断结果是否为空
        if (orders.isEmpty()) {
            throw new OrderServiceException(OrderServiceException.NO_ORDERS_MESSAGE, OrderServiceException.NO_ORDERS);
        }

        sqlSession.close();
        return orders;
    }

    /**
     * 管理员接收用户的订单（当前仅当订单已付款），订单状态变为“正在运送”状态
     *
     * @param orderId
     */
    public void acceptOrderByOrderId(String orderId) {

        //检查输入
        if (null == orderId) {

            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        //查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (null == order) {
            throw new OrderServiceException(OrderServiceException.NO_ORDERS_MESSAGE, OrderServiceException.NO_ORDERS);
        }

        //如果订单状态不是已付款状态，则抛出异常
        if (!ORDER_STATUS_HAS_PAYMENT.equals(order.getOrderStatus())) {

            throw new OrderServiceException(OrderServiceException.NO_PAY_MESSAGE, OrderServiceException.NO_PAY);
        }

        //更新订单状态为正在运送
        order.setOrderStatus(ORDER_STATUS_ON_WAY);
        orderMapper.updateByPrimaryKeySelective(order);

        sqlSession.close();
        return;
    }

    /**
     * 管理员取消用户订单，订单状态变为"已取消"状态
     *
     * @param orderId TODO:未完成
     */
    public void cancelOrderByOrderId(String orderId) {
        //检查输入
        if (null == orderId) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        //查询订单
        Order order = orderMapper.selectByPrimaryKey(orderId);
        if (null == order) {
            throw new OrderServiceException(OrderServiceException.NO_ORDERS_MESSAGE, OrderServiceException.NO_ORDERS);
        }

        //判断订单是否可以取消，在运送中或者已经取消、完成的订单不能取消
        if (ORDER_STATUS_CANCELED.equals(order.getOrderStatus()) ||
                ORDER_STATUS_ON_WAY.equals(order.getOrderStatus()) ||
                ORDER_STATUS_FINISH.equals(order.getOrderStatus())) {
            throw new OrderServiceException(OrderServiceException.ORDER_CANNT_CANCEL_MESSAGE, OrderServiceException.ORDER_CANNT_CANCEL);
        }

        //如果已经付款
        if (ORDER_STATUS_HAS_PAYMENT.equals(order.getOrderStatus())) {
            //FIXME:退款
            System.out.println("退款");
        }

        //设置订单状态为取消
        order.setOrderStatus(ORDER_STATUS_CANCELED);
        orderMapper.updateByPrimaryKeySelective(order);
        sqlSession.close();
    }

    /**
     * 根据订单id获取订单项
     * @param orderId
     * @return
     */
    public List<Product> getOrderItemByOrderId(String orderId) {

        //检查输入
        if (null == orderId) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderItemMapper orderItemMapper = sqlSession.getMapper(OrderItemMapper.class);

        OrderItemExample orderItemExample = new OrderItemExample();
        orderItemExample.createCriteria().andOrderIdEqualTo(orderId);

        List<OrderItem> orderItems = orderItemMapper.selectByExample(orderItemExample);

        //查找商品
        List<Product> products = new ArrayList<>();
        Product product;
        for (OrderItem orderItem:
                orderItems) {

            product = productService.searchProductByID(orderItem.getProductId());
            product.setProductLeftTotals(orderItem.getProductCount());

            products.add(product);
        }


        sqlSession.close();

        return products;


    }

    /**
     * 根据用户id查找订单
     *
     * @param userId
     * @return
     */
    public List<Order> searchAllOrderByUserId(String userId) {

        //检查输入
        if (null == userId) {
            throw new OrderServiceException(INVALID_INPUT_MESSAGE, OrderServiceException.INVALID_INPUT);
        }

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        //查找对应的订单
        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andUserIdEqualTo(userId);
        List<Order> orders = orderMapper.selectByExample(orderExample);

        sqlSession.close();
        return orders;
    }

    /**
     * 管理员用于获取所有订单
     * @return
     */
    public List<Order> getAllOrder(){

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        List<Order> orders = orderMapper.selectByExample(new OrderExample());

        sqlSession.close();
        return orders;
    }

    /**
     * 根据传入订单状态数组搜索符合状态的订单
     * @param statuses
     * @return
     */
    public List<Order> searchOrderByOrderStatus(String[] statuses){


        //检查输入
        if (null==statuses||0==statuses.length){

            throw new OrderServiceException(INVALID_INPUT_MESSAGE,INVALID_INPUT);
        }

        List<String> statusList = Arrays.asList(statuses);

        SqlSession sqlSession = sqlSessionFactory.openSession();
        OrderMapper orderMapper = sqlSession.getMapper(OrderMapper.class);

        OrderExample orderExample = new OrderExample();
        orderExample.createCriteria().andOrderStatusIn(statusList);

        List<Order> orders = orderMapper.selectByExample(orderExample);


        sqlSession.close();
        return orders;

    }
}

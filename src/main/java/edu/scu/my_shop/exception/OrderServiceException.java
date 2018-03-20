package edu.scu.my_shop.exception;

public class OrderServiceException extends RuntimeException{

    public static int INVALID_INPUT = 3001;
    public static int NO_ENOUGH_PRODUCT = 3002;
    public static int ORDER_CANNT_CANCEL = 3003;
    public static int NO_AUTHORITY = 3004;
    public static int NO_ORDERS = 3005;
    public static int NO_PAY = 3006;

    public static String INVALID_INPUT_MESSAGE = "无效输入";
    public static String NO_ENOUGH_PRODUCT_MESSAGE = "商品库存不足";
    public static String ORDER_CANNT_CANCEL_MESSAGE = "订单不能取消";
    public static String NO_AUTHORITY_MESSAGE = "权限不足";
    public static String NO_ORDERS_MESSAGE = "没有订单";
    public static String NO_PAY_MESSAGE = "订单未支付，不能接收";

    private int code;

    public OrderServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/19.
 */
public class CartException extends RuntimeException {
    public static final int USER_EMPTY = 401;
    public static final int PRODUCT_EMPTY = 402;
    public static final int NUMBER_EMPTY = 403;
    public static final int PRODUCT_NUMBER_NOT_MATCH = 404;
    public static final int NO_MORE_PRODUCT = 405;
    public static final int DUPLICATE_SAME_PRODUCT = 406;
    public static final int UNKNOWN_ERROR = 407;

    public static final String USER_EMPTY_MESSAGE = "未知用户";
    public static final String PRODUCT_EMPTY_MESSAGE = "未知商品";
    public static final String NUMBMER_EMPTY_MESSAGE = "商品数量错误";
    public static final String PRODUCT_NUMBER_NOT_MATCH_MESSAGE = "商品与数量不匹配";
    public static final String NO_MORE_PRODUCT_MESSAGE = "商品数量不足";
    public static final String DUPLICATE_SAME_PRODUCT_MESSAGE = "购物车中商品重复";
    public static final String UNKNOWN_ERROR_MESSAGE = "未知错误";

    private Integer code;

    public CartException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public CartException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }
}

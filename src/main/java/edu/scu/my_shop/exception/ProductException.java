package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/18.
 */
public class ProductException extends RuntimeException {
    public static final int PRODUCT_NAME_EMPTY = 301;
    public static final int PRODUCT_PRICE_EMPTY = 302;
    public static final int PRODUCT_TOTAL_EMPTY = 303;
    public static final int PRODUCT_CATEGORY_EMPTY = 304;
    public static final int PRODUCT_CATEGORY_ERROR = 305;
    public static final int PRODUCT_ID_EMPTY = 306;
    public static final int PRODUCT_IS_NULL = 307;
    public static final int SEARCH_RESULT_IS_NULL = 308;
    public static final int UNKNOWN_ERROR = 666;

    public static final String PRODUCT_NAME_EMPTY_MESSAGE = "商品名称为空";
    public static final String PRODUCT_PRICE_EMPTY_MESSAGE = "商品价格为空";
    public static final String PRODUCT_TOTAL_EMPTY_MESSAGE = "商品剩余量为空";
    public static final String PRODUCT_CATEGORY_EMPTY_MESSAGE = "Product secondary id is empty.";
    public static final String PRODUCT_CATEGORY_ERROR_MESSAGE = "No such second category.";
    public static final String PRODUCT_ID_EMPTY_MESSAGE = "Product ID is empty";
    public static final String PRODUCT_IS_NULL_MESSAGE = "Product is null.";
    public static final String SEARCH_RESULT_IS_NULL_MESSAGE = "查询结果为空";
    public static final String UNKNOWN_ERROR_MESSAGE = "Unknown product error";

    private Integer code;

    public ProductException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public ProductException(String message, Integer code) {
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

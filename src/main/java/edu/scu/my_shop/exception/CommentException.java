package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
public class CommentException extends RuntimeException{

    public static final int NULL_COMMENT = 701;
    public static final int COMMENT_ERROR = 702;
    public static final int PRODUCT_ERROR = 703;
    public static final int USER_ERROR = 704;
    public static final int UNKNOWN_ERROR = 705;

    public static final String NULL_COMMENT_MESSAGE = "未知评论";
    public static final String COMMENT_ERROR_MESSAGE = "评论为空";
    public static final String PRODUCT_ERROR_MESSAGE = "未知商品";
    public static final String USER_ERROR_MESSAGE = "未知用户";
    public static final String UNKNOWN_ERROR_MESSAGE = "未知错误";

    private Integer code;

    public CommentException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public CommentException(String message, Integer code) {
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

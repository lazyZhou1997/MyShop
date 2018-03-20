package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
public class MessageException extends RuntimeException {

    public static final int USER_EMPTY = 601;
    public static final int NULL_MESSAGE = 602;
    public static final int UNKNOWN_ERROR = 603;

    public static final String USER_EMPTY_MESSAGE = "未知用户";
    public static final String NULL_MESSAGE_MESSAGE = "信息为NULL";
    public static final String UNKNOWN_ERROR_MESSAGE = "未知错误";

    private Integer code;

    public MessageException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public MessageException(String message, Integer code) {
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

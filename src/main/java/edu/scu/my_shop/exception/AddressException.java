package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/20.
 */
public class AddressException extends RuntimeException {


    public static final int USER_EMPTY = 501;
    public static final int ADDRESS_EMPTY = 502;
    public static final int UNKNOWN_ERROR = 503;

    public static final String USER_EMPTY_MESSAGE = "未知用户";
    public static final String ADDRESS_EMPTY_MESSAGE = "未知地址";
    public static final String UNKNOWN_ERROR_MESSAGE = "未知错误";

    private Integer code;

    public AddressException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public AddressException(String message, Integer code) {
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

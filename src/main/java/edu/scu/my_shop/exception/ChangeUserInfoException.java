package edu.scu.my_shop.exception;

/**
 * 修改用户信息异常
 */
public class ChangeUserInfoException extends RuntimeException{

    public static int USERNAME_HAS_EXIST = 1001;


    public static String USERNAME_HAS_EXIST_MESSAGE = "用户名已经存在";

    /**
     * 错误码
     */
    private Integer code;

    public ChangeUserInfoException(String message, int code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}

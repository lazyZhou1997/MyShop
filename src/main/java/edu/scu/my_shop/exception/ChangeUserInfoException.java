package edu.scu.my_shop.exception;

/**
 * 修改用户信息异常
 */
public class ChangeUserInfoException extends RuntimeException{

    public static int USERNAME_HAS_EXIST = 1001;
    public static int NO_VALUE = 1002;
    public static int INVALID_INPUT = 1003;
    public static int NO_USER = 1004;


    public static String USERNAME_HAS_EXIST_MESSAGE = "用户名已经存在";
    public static String NO_VALUE_MESSAGE = "没有要修改的值";
    public static String INVALID_INPUT_MESSAGE = "非法输入";
    public static String NO_USER_MESSAGE = "用户不存在";

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

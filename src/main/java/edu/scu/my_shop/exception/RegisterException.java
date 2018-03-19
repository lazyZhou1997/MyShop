package edu.scu.my_shop.exception;

/**
 * 登录异常
 */
public class RegisterException extends RuntimeException {

    public static int INVALID_INPUT = 101;
    public static int EMAIL_HAS_EXIST = 102;
    public static int USERNAME_HAS_EXIST = 103;


    public static String INVALID_INPUT_MESSAGE = "无效输入";
    public static String EMAIL_HAS_EXIST_MESSAGE = "邮箱已经存在";
    public static String USERNAME_HAS_EXIST_MESSAGE = "用户名已经存在";

    /**
     * 错误码
     */
    private Integer code;



    public RegisterException(String message, int code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}

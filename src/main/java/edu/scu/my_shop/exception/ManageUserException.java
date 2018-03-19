package edu.scu.my_shop.exception;

public class ManageUserException extends RuntimeException{

    public static int INVALID_INPUT = 2001;

    public static String INVALID_INPUT_MESSAGE = "无效输入";

    /**
     * 错误码
     */
    private int code;

    public ManageUserException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

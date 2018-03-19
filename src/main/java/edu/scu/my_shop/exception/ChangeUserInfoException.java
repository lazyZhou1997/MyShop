package edu.scu.my_shop.exception;

/**
 * 修改用户信息异常
 */
public class ChangeUserInfoException extends RuntimeException{

    /**
     * 错误码
     */
    private Integer code = 101;

    public ChangeUserInfoException(String message) {
        super(message);
    }

    public Integer getCode() {
        return code;
    }
}

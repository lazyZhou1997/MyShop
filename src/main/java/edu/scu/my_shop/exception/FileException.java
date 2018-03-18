package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/18.
 */
public class FileException extends RuntimeException {
    public static final int EMPTY_FILE = 201;
    public static final int NETWORK_EXCEPTION = 202;
    public static final int IO_EXCEPTION = 203;
    public static final int UNKNOWN_ERROR = 204;

    private Integer code;

    public FileException(String message) {
        super(message);
        this.code = UNKNOWN_ERROR;
    }

    public FileException(String message, Integer code) {
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

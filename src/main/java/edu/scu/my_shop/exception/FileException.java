package edu.scu.my_shop.exception;

/**
 * Created by Vicent_Chen on 2018/3/18.
 */
public class FileException extends RuntimeException {
    public static final int EMPTY_FILE = 201;
    public static final int NETWORK_EXCEPTION = 202;
    public static final int IO_EXCEPTION = 203;
    public static final int QUERY_PID_ERROR = 204;
    public static final int IMAGE_ID_EMPTY = 205;
    public static final int IMAGE_URL_EMPTY = 206;
    public static final int IMAGE_ID_FILE_NOT_MATCH = 207;
    public static final int EMPTY_USER = 208;
    public static final int UNKNOWN_ERROR = 209;

    public static final String EMPTY_FILE_MESSAGE = "文件为空";
    public static final String NETWORK_EXCEPION_MESSAGE = "网络异常";
    public static final String IO_EXCEPTION_MESSAGE = "服务器文件写入错误";
    public static final String QUERY_PID_ERROR_MESSAGE = "商品ID为空";
    public static final String IMAGE_ID_EMPTY_MESSAGE = "图片ID为空";
    public static final String IMAGE_URL_EMPTY_MESSAGE = "图片URL为空";
    public static final String IMAGE_ID_FILE_NOT_MATCH_MESSAGE = "图片ID与文件不匹配";
    public static final String EMPTY_USER_MESSAGE = "未知用户";
    public static final String UNKNOW_ERROR_MESSAGE = "未知错误";

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

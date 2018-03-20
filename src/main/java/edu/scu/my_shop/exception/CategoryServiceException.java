package edu.scu.my_shop.exception;

public class CategoryServiceException extends RuntimeException{

    public static int INVALID_INPUT = 4001;
    public static int FIRST_CATEGORYNAME_EXIST = 4002;
    public static int NO_FIRST_CATEGORYNAME = 4003;
    public static int NO_SECOND_CATEGORYNAME = 4004;

    public static String INVALID_INPUT_MESSAGE = "无效输入";
    public static String FIRST_CATEGORYNAME_EXIST_MESSAGE = "一级分类已经存在";
    public static String NO_FIRST_CATEGORYNAME_MESSAGE = "没有一级分类";
    public static String NO_SECOND_CATEGORYNAME_MESSAGE = "没有二级分类";

    /**
     * 错误码
     */
    private int code;

    public CategoryServiceException(String message, int code) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}

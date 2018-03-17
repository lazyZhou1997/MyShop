package edu.scu.my_shop.result;


import edu.scu.my_shop.enums.ExceptionEnum;

/**
 * @Author: 周秦春
 * @Description: 封装返回结果的类,Http访问返回的最外层
 * @Date: Create in 2017/8/22 20:32
 * @ModifyBy:
 */
public class Result {

    /**
     * 返回结果状态码
     */
    private Integer code;

    /**
     * 错误内容提示信息
     */
    private String message;

    /**
     * 返回的具体内容
     */
    private Object data;

    /**
     * 无参构造
     */
    public Result(){}

    /**
     * 带有两个参数的构造函数
     * @param exceptionEnum 返回信息
     * @param data 传输的数据
     */
    public Result(ExceptionEnum exceptionEnum, Object data){
        this.code = exceptionEnum.getCode();
        this.message = exceptionEnum.getMessgae();
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}

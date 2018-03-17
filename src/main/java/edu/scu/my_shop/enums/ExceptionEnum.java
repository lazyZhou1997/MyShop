package edu.scu.my_shop.enums;


/**
 * @Author: 周秦春
 * @Description:
 * @Date: Create in 2017/8/23 9:44
 * @ModifyBy:
 */
public enum ExceptionEnum {
    ;
    /**
     * 异常代码
     */
    private Integer code;

    /**
     * 异常信息
     */
    private String messgae;

    ExceptionEnum(Integer code, String messgae) {
        this.code = code;
        this.messgae = messgae;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessgae() {
        return messgae;
    }

    public void setMessgae(String messgae) {
        this.messgae = messgae;
    }
}

package edu.scu.my_shop.exceptionHandle;

import edu.scu.my_shop.exception.RegisterException;
import edu.scu.my_shop.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 */
@ControllerAdvice
public class ExceptionHandle {

    /**
     * 注册相关的异常处理
     * @param reg
     * @param e
     * @return
     */
    @ExceptionHandler(RegisterException.class)
    @ResponseBody
    public Result rigesterExceptionHandle(HttpServletRequest reg, RegisterException e){

        //构建异常处理结果
        Result result = new Result();
        result.setCode(e.getCode());
        result.setMessage(e.getMessage());
        result.setData(null);

        //返回异常处理结果
        return result;
    }
}

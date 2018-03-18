package edu.scu.my_shop.exceptionHandle;

import edu.scu.my_shop.exception.RegisterException;
import edu.scu.my_shop.result.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * 异常处理
 */
@ControllerAdvice
public class ExceptionHandle {

//    @Autowired
//    private ModelMap map;


    /**
     * 注册相关的异常处理
     * @param reg
     * @param e
     * @return
     */
    @ExceptionHandler(RegisterException.class)
    public ModelAndView rigesterExceptionHandle(HttpServletRequest reg, RegisterException e){

        ModelAndView mav = new ModelAndView();
        mav.getModelMap().addAttribute("message",e.getMessage());//携带属性
        mav.setViewName("signup");//返回页面
        return mav;
    }
}

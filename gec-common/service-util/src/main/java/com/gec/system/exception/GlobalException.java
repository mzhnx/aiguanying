package com.gec.system.exception;


import com.gec.system.util.Result;
import com.gec.system.util.ResultCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalException  {
    private final static Logger log = LoggerFactory.getLogger(GlobalException.class);

    // 全局异常处理
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result error(Exception e)
    {
        System.out.println(" public Result error(Exception e)...................");
        log.error("未知异常"+e.getMessage());
        return Result.fail().message("执行了全局异常..");
    }
    // 特定异常
    @ExceptionHandler(ArithmeticException.class)
    @ResponseBody
    public Result error(ArithmeticException e)
    {
        System.out.println("public Result error(ArithmeticException e)..............");
        log.error("特定异常"+e.getMessage());
        return Result.fail().message("执行了特定异常...");
    }
    // 自定义异常
    @ExceptionHandler(MyCustomerException.class)
    @ResponseBody
    public Result  error(MyCustomerException e)
    {
        log.error("自定义异常"+e.getMessage());
        return Result.fail().code(e.getCode()).message(e.getMessage());
    }


    /**
     * spring security异常
     * @param e
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public Result error(AccessDeniedException e) throws AccessDeniedException {
        log.error("没有此操作权限"+e.getMessage());
        return Result.fail().code(ResultCodeEnum.PERMISSION.getCode()).message("没有当前操作权限");
    }

}

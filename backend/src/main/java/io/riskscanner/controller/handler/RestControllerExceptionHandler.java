package io.riskscanner.controller.handler;


import io.riskscanner.commons.exception.RSException;
import io.riskscanner.controller.ResultHolder;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@RestControllerAdvice
public class RestControllerExceptionHandler {
    /*=========== Shiro 异常拦截==============*/
    @ExceptionHandler(ShiroException.class)
    public ResultHolder exceptionHandler(HttpServletRequest request, HttpServletResponse response, java.lang.Exception exception) {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return ResultHolder.error(exception.getMessage());
    }


    @ExceptionHandler(RSException.class)
    public ResultHolder msExceptionHandler(HttpServletRequest request, HttpServletResponse response, RSException e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        return ResultHolder.error(e.getMessage());
    }
}

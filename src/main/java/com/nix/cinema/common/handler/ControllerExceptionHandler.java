package com.nix.cinema.common.handler;

import com.nix.cinema.Exception.WebException;
import com.nix.cinema.common.ReturnObject;
import com.nix.cinema.util.ReturnUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 11723
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = WebException.class)
    public ReturnObject controllerHandle(WebException e) {
        e.printStackTrace();
        return ReturnUtil.fail(e.getCode(),e.getMessage(),null);
    }
    @ResponseBody
    @ExceptionHandler(value = IllegalArgumentException.class)
    public ReturnObject exceptionHandle(IllegalArgumentException e) {
        e.printStackTrace();
        return ReturnUtil.fail(-1,e.getMessage(),null);
    }

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public ReturnObject exceptionHandle(Exception e) {
        e.printStackTrace();
        return ReturnUtil.fail(-1,"未知错误",null);
    }
}

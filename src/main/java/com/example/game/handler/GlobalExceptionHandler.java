package com.example.game.handler;

import com.alibaba.fastjson.JSONObject;
import com.example.game.support.ResultMsg;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public void exception(HttpServletRequest request, HttpServletResponse response,
                          HttpRequestMethodNotSupportedException e) {
        log.error("Method Not Support：{} -> {}", request.getRequestURL(), e.getMessage());
        responseMsg(response, e.getMessage());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public void exception(HttpServletRequest request, HttpServletResponse response,
                          MissingServletRequestParameterException e) {
        log.error("Parameter Missing：{} -> {}", request.getRequestURL(), e.getMessage());
        responseMsg(response, e.getMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public void exception(HttpServletRequest request, HttpServletResponse response,
                          MethodArgumentTypeMismatchException e) {
        log.error("Parameter Mismatch：{} -> {}", request.getRequestURL(), e.getMessage());
        responseMsg(response, e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public void exception(HttpServletRequest request, HttpServletResponse response, IllegalArgumentException e) {
        log.error("Illegal Argument：{} -> {}", request.getRequestURL(), e.getMessage());
        responseMsg(response, e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public void exception(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("System error：{}", request.getRequestURL(), e);
        responseMsg(response, e.getMessage());
    }

    private void responseMsg(HttpServletResponse response, String message) {
        try {
            response.setStatus(HttpStatus.OK.value());
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write(JSONObject.toJSONString(ResultMsg.failure(500, message)));
        } catch (Exception e) {
            log.error("GlobalExceptionHandler error", e);
        }
    }
}

package com.example.game.support;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResultMsg<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final Integer OK = 200;
    public static final Integer ERROR = 500;

    // return code
    private Integer code;
    // return message
    private String message;

    // return data
    private T data;

    public ResultMsg(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResultMsg<T> success() {
        return new ResultMsg<>(OK, "ok", null);
    }

    public static <T> ResultMsg<T> success(T data) {
        return new ResultMsg<>(OK, "ok", data);
    }

    public static <T> ResultMsg<T> success(String message, T data) {
        return new ResultMsg<>(OK, message, data);
    }

    public static <T> ResultMsg<T> success(Integer code, String message, T data) {
        return new ResultMsg<>(code, message, data);
    }

    public static ResultMsg<String> failure(String message) {
        return new ResultMsg<>(ERROR, message, message);
    }

    public static <T> ResultMsg<T> failure(Integer code, String message) {
        return new ResultMsg<>(code, message, null);
    }

    public static <T> ResultMsg<T> failure(String message, T data) {
        return new ResultMsg<>(ERROR, message, data);
    }

}

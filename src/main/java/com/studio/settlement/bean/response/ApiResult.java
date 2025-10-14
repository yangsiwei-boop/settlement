package com.studio.settlement.bean.response;

import lombok.Data;

@Data
public class ApiResult<T> {

    public static final int CODE_SUCCESS = 200;

    public static final int CODE_ERROR = 500;

    public static final String MESSAGE_SUCCESS = "操作成功";

    public static final String MESSAGE_FAIL = "操作失败";

    private Integer code;

    private String msg;

    private T data;

    public ApiResult() {
    }

    public ApiResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static ApiResult ok() {
        return new ApiResult(CODE_SUCCESS, "ok", null);
    }

    public static ApiResult ok(String msg) {
        return new ApiResult(CODE_SUCCESS, msg, null);
    }

    public static ApiResult code(int code) {
        return new ApiResult(code, null, null);
    }

    public static ApiResult data(Object data) {
        return new ApiResult(CODE_SUCCESS, "ok", data);
    }

    public static ApiResult error() {
        return new ApiResult(CODE_ERROR, "error", null);
    }

    public static ApiResult error(String msg) {
        return new ApiResult(CODE_ERROR, msg, null);
    }

    public static ApiResult error(int code, String msg) {
        return new ApiResult(code, msg, null);
    }

    public static ApiResult error(int code, String msg, Object data) {
        return new ApiResult(code, msg, data);
    }

    public static ApiResult get(int code, String msg, Object data) {
        return new ApiResult(code, msg, data);
    }

    public static ApiResult status(Boolean status) {
        return status ? new ApiResult(CODE_SUCCESS, MESSAGE_SUCCESS, null) : new ApiResult(CODE_ERROR, MESSAGE_FAIL, null);
    }

    public static ApiResult status(Boolean status, String message) {
        return status ? new ApiResult(CODE_SUCCESS, message, null) : new ApiResult(CODE_ERROR, message, null);
    }
}

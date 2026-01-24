package com.campus.activity.Vo;

import lombok.Data;

@Data
public class ResultVo<T> {
    private Integer code;       // 响应码：200-成功 500-失败
    private String msg;        // 响应信息
    private T data;            // 响应数据

    // 成功响应
    public static <T> ResultVo<T> success(String msg, T data) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(200);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    // 成功响应（无数据）
    public static <T> ResultVo<T> success(String msg) {
        return success(msg, null);
    }

    // 失败响应
    public static <T> ResultVo<T> error(String msg) {
        ResultVo<T> result = new ResultVo<>();
        result.setCode(500);
        result.setMsg(msg);
        result.setData(null);
        return result;
    }
}
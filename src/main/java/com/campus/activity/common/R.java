package com.campus.activity.common;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 全局统一返回对象
 * @param <T> 实际业务数据类型
 */
@Data
@Accessors(chain = true)
public class R<T> {
    private int    code;
    private String msg;
    private T      data;

    /* ===== 成功：主流命名（推荐） ===== */
    public static <T> R<T> success() {
        return new R<T>().setCode(200).setMsg("success");
    }

    public static <T> R<T> success(T data) {
        return new R<T>().setCode(200).setMsg("success").setData(data);
    }

    /* ===== 成功：兼容常用命名 ok() ===== */
    public static <T> R<T> ok() {
        return success();
    }

    public static <T> R<T> ok(T data) {
        return success(data);
    }

    /* ===== 失败 ===== */
    public static <T> R<T> fail(String msg) {
        return new R<T>().setCode(500).setMsg(msg);
    }

    /* ===== 失败别名（兼容旧代码） ===== */
    public static <T> R<T> error(String msg) {
        return fail(msg);
    }
}
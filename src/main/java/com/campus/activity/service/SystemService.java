package com.campus.activity.service;
import com.campus.activity.dto.LoginRequest;
import com.campus.activity.dto.LoginResponse;

/**
 * 系统管理业务接口
 */
public interface SystemService {

    /**
     * 管理端登录
     * @param req 登录请求参数
     * @return 登录成功信息（含 JWT） —— 注意：返回的是裸 DTO，不是 R<?>
     * @throws RuntimeException 用户名或密码错误时抛出
     */
    LoginResponse login(LoginRequest req);
}
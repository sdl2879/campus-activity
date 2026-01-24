package com.campus.activity.service.impl;

import com.campus.activity.utils.JwtUtil;
import org.springframework.stereotype.Service;

import jakarta.annotation.Resource;

@Service
public class UserServiceImpl {

    // 注入 JwtUtil 实例（核心：通过 Spring 注入，而非静态调用）
    @Resource
    private JwtUtil jwtUtil;

    /**
     * 生成用户 Token 的示例方法
     */
    public String generateUserToken(Long userId) {
        // 正确调用：通过实例调用 createToken 方法
        return jwtUtil.createToken(userId);
    }
}
package com.campus.activity.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 * 功能：生成登录Token、解析Token、校验Token有效性
 * 配合 Redis 使用：Token 生成后缓存到 Redis，退出登录时删除缓存
 */
@Component
public class JwtUtil {

    // JWT 密钥（配置在 application.yml，建议长度≥32位，避免解密）
    @Value("${jwt.secret:campus-admin-2026-secret-key-1234567890}")
    private String secret;

    // Token 过期时间（单位：毫秒，默认2小时）
    @Value("${jwt.expire:7200000}")
    private long expire;

    /**
     * 生成登录 Token
     * @param userId 用户ID（用于缓存和解析）
     * @param username 用户名（可选载荷）
     * @return JWT Token 字符串
     */
    public String generateToken(Long userId, String username) {
        // 1. 构建 Token 载荷（存放用户核心信息，避免敏感数据）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);       // 用户ID（核心标识）
        claims.put("username", username);   // 用户名（非敏感）

        // 2. 生成加密密钥（基于 HMAC-SHA256）
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        // 3. 构建 Token（设置载荷、签发时间、过期时间、签名）
        return Jwts.builder()
                .setClaims(claims)                // 载荷
                .setIssuedAt(new Date())          // 签发时间
                .setExpiration(new Date(System.currentTimeMillis() + expire)) // 过期时间
                .signWith(key)                    // 签名（防篡改）
                .compact();
    }

    /**
     * 解析 Token，获取载荷信息
     * @param token JWT Token 字符串
     * @return 载荷对象（包含用户ID、用户名等）
     * @throws Exception Token 非法/过期时抛出异常
     */
    public Claims parseToken(String token) {
        SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
        return Jwts.parserBuilder()
                .setSigningKey(key)          // 设置验签密钥
                .build()
                .parseClaimsJws(token)       // 解析 Token
                .getBody();                  // 获取载荷
    }

    /**
     * 校验 Token 是否过期
     * @param token JWT Token
     * @return true=已过期，false=有效
     */
    public boolean isTokenExpired(String token) {
        return parseToken(token).getExpiration().before(new Date());
    }

    /**
     * 从 Token 中提取用户ID
     * @param token JWT Token
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        return parseToken(token).get("userId", Long.class);
    }
}
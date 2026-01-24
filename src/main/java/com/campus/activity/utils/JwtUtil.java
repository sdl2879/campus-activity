package com.campus.activity.utils;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException; // 替换过时的异常类
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

/**
 * JWT 工具类
 */
@Component // Spring Bean 注解
public class JwtUtil {

    // 固定密钥（至少32位，正式环境建议放application.yml）
    private static final String SECRET_KEY = "campus-activity-jwt-secret-key-1234567890";
    private static final SecretKey KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // 过期时间 1 天
    private static final long EXPIRATION = 86400000;

    /**
     * 生成 Token（实例方法，需通过 JwtUtil 实例调用）
     * @param userId 用户主键
     * @return JWT 字符串
     */
    public String createToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    /**
     * 解析并验证 Token（实例方法）
     * @param token JWT 字符串
     * @return 用户主键（解析失败返回 null）
     */
    public Long parseToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return Long.valueOf(claims.getSubject());
        } catch (ExpiredJwtException e) {
            // Token 过期
            return null;
        } catch (MalformedJwtException e) {
            // Token 格式错误
            return null;
        } catch (SignatureException e) { // 使用新的 SignatureException
            // 签名错误
            return null;
        } catch (IllegalArgumentException e) {
            // 参数错误（如 Token 为空）
            return null;
        } catch (JwtException e) {
            // 其他 JWT 解析异常
            return null;
        }
    }
}
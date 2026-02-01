package com.campus.activity.utils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Date;
/**
 * JWT工具类（包含parseToken/parseAdminToken，支持静态+实例调用）
 */
@Component // 注册为Spring Bean，支持@Resource注入
public class JwtUtil {
    // 密钥（长度≥32位，避免签名失败）
    private static final String SECRET_KEY = "campusActivitySecretKey123456789012345678901234";
    // Token过期时间：24小时
    private static final long EXPIRATION_TIME = 24 * 60 * 60 * 1000;
    // ========== 通用Token生成/解析 ==========
    // 生成通用Token
    public static String generateToken(Long userId, String username) {
        SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
        return Jwts.builder()
                .claim("userId", userId)
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(key)
                .compact();
    }
    // 解析通用Token（解决「找不到parseToken」错误）
    public static Claims parseToken(String token) {
        try {
            String realToken = token != null && token.startsWith("Bearer ") ? token.substring(7) : token;
            SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(realToken)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }
    // ========== 管理员Token生成/解析 ==========
    // 生成管理员Token
    public static String generateAdminToken(Long adminId, String username) {
        return generateToken(adminId, username); // 复用通用生成逻辑
    }
    // 解析管理员Token
    public static Claims parseAdminToken(String token) {
        return parseToken(token); // 复用通用解析逻辑
    }
    // ========== 通用工具方法 ==========
    // 检查Token是否过期
    public static boolean isTokenExpired(Claims claims) {
        return claims != null && claims.getExpiration().before(new Date());
    }
    // 从Claims获取用户ID（Long类型）
    public static Long getUserIdFromClaims(Claims claims) {
        return claims == null ? null : claims.get("userId", Long.class);
    }
    // 从Claims获取管理员ID（Long类型）
    public static Long getAdminIdFromClaims(Claims claims) {
        return getUserIdFromClaims(claims); // 复用用户ID获取逻辑
    }
    // 从Claims获取用户名
    public static String getUsernameFromClaims(Claims claims) {
        return claims == null ? null : claims.get("username", String.class);
    }
    // ========== 实例方法（兼容注入调用，避免与静态方法命名冲突） ==========
    // 实例版解析Token（兼容jwtUtil.parseTokenInstance()调用）
    public Claims parseTokenInstance(String token) {
        return parseToken(token);
    }
    // 实例版解析AdminToken（兼容jwtUtil.parseAdminTokenInstance()调用）
    public Claims parseAdminTokenInstance(String token) {
        return parseAdminToken(token);
    }
}
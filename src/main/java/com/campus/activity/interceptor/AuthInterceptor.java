package com.campus.activity.interceptor;
import com.campus.activity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

// 关键：Spring Boot 3.x 用 jakarta.servlet 包，替换 javax.servlet
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Token鉴权拦截器（实现HandlerInterceptor接口，补全所有抽象方法）
 */
@Component
public class AuthInterceptor implements HandlerInterceptor { // 实现接口

    // 核心Token校验逻辑
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 放行登录接口
        String requestURI = request.getRequestURI();
        if (requestURI.contains("/api/user/admin/login")) {
            return true;
        }

        // 获取Token（兼容Authorization和token字段）
        String token = request.getHeader("Authorization");
        if (token == null || token.trim().isEmpty()) {
            token = request.getHeader("token");
        }

        // 校验Token存在性
        if (token == null || token.trim().isEmpty()) {
            return send401Response(response, "未携带Token，请登录");
        }

        // 解析并校验Token
        Claims claims = JwtUtil.parseToken(token);
        if (claims == null || JwtUtil.isTokenExpired(claims)) {
            return send401Response(response, "Token无效或已过期");
        }

        // 存储用户信息到请求属性
        request.setAttribute("adminId", JwtUtil.getAdminIdFromClaims(claims));
        return true;
    }

    // 补全接口其他方法（避免"未实现超类型方法"报错）
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 发送401未授权响应
     */
    private boolean send401Response(HttpServletResponse response, String msg) throws Exception {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        PrintWriter writer = response.getWriter();
        writer.write("{\"code\":401,\"msg\":\"" + msg + "\",\"data\":null}");
        writer.flush();
        writer.close();
        return false;
    }
}
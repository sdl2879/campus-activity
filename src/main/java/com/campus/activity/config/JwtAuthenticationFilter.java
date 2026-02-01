package com.campus.activity.config;

import com.campus.activity.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // 放行列表
    private static final String[] WHITE_LIST = {
            "/api/user/admin/login",
            "/api/user/student/login",
            "/api/common/**",
            "/uploads/**",
            "/swagger-ui/**",
            "/v3/api-docs/**"
    };

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String uri = request.getRequestURI();
        System.out.println("收到请求: " + uri);

        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isWhiteList(uri)) {
            System.out.println("✅ 放行白名单: " + uri);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = getTokenFromRequest(request);
            if (!StringUtils.hasText(token)) {
                System.out.println("❌ 拒绝: 无Token, URI=" + uri);
                sendErrorResponse(response, 401, "未登录，请先登录");
                return;
            }

            Claims claims = JwtUtil.parseToken(token);
            if (claims == null || JwtUtil.isTokenExpired(claims)) {
                sendErrorResponse(response, 401, "Token已过期或无效");
                return;
            }

            String username = JwtUtil.getUsernameFromClaims(claims);
            Long userId = JwtUtil.getUserIdFromClaims(claims);

            if (username == null || userId == null) {
                sendErrorResponse(response, 401, "Token解析失败");
                return;
            }

            // 🔥🔥 关键修改：加载用户权限（临时硬编码）
            List<String> permissions = getPermissionsByUserId(userId); // 临时返回所有角色管理权限

            List<GrantedAuthority> authorities = permissions.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());

            UserDetails userDetails = User.builder()
                    .username(username)
                    .password("")
                    .authorities(authorities)
                    .build();

            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            filterChain.doFilter(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            sendErrorResponse(response, 500, "认证失败：" + e.getMessage());
        }
    }

    // 🔥 临时硬编码权限（测试用）—— 所有登录用户都拥有角色管理权限
    private List<String> getPermissionsByUserId(Long userId) {
        return Arrays.asList(
                "sys:role:view",
                "sys:role:list",
                "sys:role:add",
                "sys:role:edit"
        );
    }

    private void sendErrorResponse(HttpServletResponse response, int code, String msg) throws IOException {
        response.setStatus(code);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(String.format("{\"code\":%d,\"msg\":\"%s\"}", code, msg));
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private boolean isWhiteList(String requestUri) {
        for (String whiteUri : WHITE_LIST) {
            if (whiteUri.endsWith("/**")) {
                String prefix = whiteUri.substring(0, whiteUri.length() - 3);
                if (requestUri.startsWith(prefix)) {
                    return true;
                }
            } else {
                if (requestUri.equals(whiteUri)) {
                    return true;
                }
            }
        }
        return false;
    }
}
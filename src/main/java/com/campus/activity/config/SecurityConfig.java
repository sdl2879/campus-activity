package com.campus.activity.config;

import com.campus.activity.config.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // ← 新增导入
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import jakarta.annotation.Resource;
import java.util.Arrays;

/**
 * Spring Security配置类（集成JWT过滤器）
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Resource
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // 🔥🔥 关键修复：提供 BCryptPasswordEncoder Bean
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. 关闭CSRF（前后端分离无需）
                .csrf(csrf -> csrf.disable())
                // 2. 配置跨域（解决前端跨域问题）
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // 3. 关闭Session（JWT无状态认证）
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 4. 配置请求授权
                .authorizeHttpRequests(auth -> auth
                        // 放行所有OPTIONS请求（跨域预检）
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 放行白名单接口
                        .requestMatchers("/api/user/admin/login", "/api/user/student/login").permitAll()
                        .requestMatchers("/api/common/**", "/uploads/**").permitAll()
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        // 所有其他请求需要认证
                        .anyRequest().authenticated()
                )
                // 5. 添加JWT过滤器（在用户名密码过滤器之前执行）
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * 跨域配置（解决前端跨域请求问题）
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名（生产环境替换为具体域名）
        config.addAllowedOriginPattern("*");
        // 允许所有请求方法
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // 显式放行Authorization头（前端携带Token需要）
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type", "Accept"));
        // 允许携带Cookie/Token
        config.setAllowCredentials(true);
        // 预检请求缓存时间
        config.setMaxAge(3600L);
        // 暴露Authorization响应头（前端可获取）
        config.addExposedHeader("Authorization");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * 补充UserDetailsService Bean（消除警告的核心）
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            throw new UsernameNotFoundException("JWT认证模式下，不支持用户名密码查询");
        };
    }
}
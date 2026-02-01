package com.campus.activity.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
/**
 * 全局跨域配置（适配前端3000端口，兼容所有Spring Boot版本）
 */
@Configuration
public class CorsConfig {
    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 关键修复：改用 allowedOriginPatterns 兼容所有版本（替代 allowedOrigin）
        config.addAllowedOriginPattern("*");
        // 保留：允许携带Cookie/Token（必须开启，否则Authorization头失效）
        config.setAllowCredentials(true);
        // 允许所有HTTP请求方法（GET/POST/PUT/DELETE等）
        config.addAllowedMethod("*");
        // 允许所有请求头（包括Authorization/Content-Type等）
        config.addAllowedHeader("*");
        // 可选：预检请求缓存时间（减少OPTIONS请求次数）
        config.setMaxAge(3600L);
        // 应用到所有接口（/** 表示匹配所有路径）
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
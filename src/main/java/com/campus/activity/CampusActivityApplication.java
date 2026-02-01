package com.campus.activity;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity; // ✅ 正确导入

@SpringBootApplication
@MapperScan("com.campus.activity.mapper")
@EnableMethodSecurity // ✅ 启用方法级安全（Spring Security 6+）
public class CampusActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusActivityApplication.class, args);
    }
}
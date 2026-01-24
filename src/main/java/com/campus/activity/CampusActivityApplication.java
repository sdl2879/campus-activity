package com.campus.activity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 自动扫描 com.campus.activity 及其子包下的所有 @Component 注解类
@SpringBootApplication
public class CampusActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(CampusActivityApplication.class, args);
    }
}
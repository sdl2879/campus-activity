package com.campus.activity.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 头像上传后可通过URL访问（关键：映射本地路径到HTTP路径）
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    // 本地头像存储路径（自行修改为你的服务器路径，比如Linux:/usr/upload/avatar/）
    private static final String AVATAR_LOCAL_PATH = "D:/upload/avatar/";
    // 前端访问头像的URL前缀
    private static final String AVATAR_URL_PREFIX = "/avatar/";

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 映射规则：http://localhost:8080/avatar/xxx.jpg → D:/upload/avatar/xxx.jpg
        registry.addResourceHandler(AVATAR_URL_PREFIX + "**")
                .addResourceLocations("file:" + AVATAR_LOCAL_PATH);
    }
}
package com.campus.activity.config;
import com.campus.activity.interceptor.AuthInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
/**
 * Web配置（注册拦截器）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Autowired
    private AuthInterceptor authInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册鉴权拦截器，拦截所有/api开头的接口
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/api/**") // 拦截所有/api接口
                .excludePathPatterns("/api/user/admin/login"); // 放行登录接口
    }
}
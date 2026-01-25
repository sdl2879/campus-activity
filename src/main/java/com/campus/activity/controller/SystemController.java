package com.campus.activity.controller;
import com.campus.activity.common.R;
import com.campus.activity.dto.LoginRequest;
import com.campus.activity.dto.LoginResponse;
import com.campus.activity.service.SystemService;
import jakarta.annotation.Resource;   // JDK17 必须用 jakarta
import org.springframework.web.bind.annotation.*;

/**
 * 管理端登录 & 系统基础接口
 */
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = "*")   // 临时放行跨域，后续可改配置
public class SystemController {

    /* 注入业务层（接口形式，方便扩展） */
    @Resource
    private SystemService systemService;

    /**
     * 登录接口
     * 返回 R<?> 可兼容成功/失败两种泛型
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody LoginRequest req) {
        try {
            LoginResponse resp = systemService.login(req);
            return R.success(resp);          // 200 成功
        } catch (Exception e) {
            return R.error(e.getMessage());  // 500 失败
        }
    }
}
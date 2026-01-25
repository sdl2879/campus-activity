package com.campus.activity.controller;

import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.service.SysConfigService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;

/**
 * 基础信息配置接口
 */
@RestController
@RequestMapping("/api/system/config")
public class SysConfigController {
    @Resource
    private SysConfigService sysConfigService;

    // 获取系统配置
    @GetMapping
    public SysConfig getConfig() {
        return sysConfigService.getSystemConfig();
    }

    // 更新系统配置
    @PutMapping
    public Boolean updateConfig(@RequestBody SysConfig config) {
        // 若配置不存在则新增，存在则更新
        if (config.getId() == null) {
            return sysConfigService.save(config);
        }
        return sysConfigService.updateById(config);
    }
}
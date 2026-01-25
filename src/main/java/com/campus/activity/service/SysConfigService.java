package com.campus.activity.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysConfig;

public interface SysConfigService extends IService<SysConfig> {
    // 获取系统唯一配置
    SysConfig getSystemConfig();
}
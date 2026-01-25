package com.campus.activity.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysOperLog;

public interface SysOperLogService extends IService<SysOperLog> {
    // 清空所有日志
    Boolean clearAllLog();
}
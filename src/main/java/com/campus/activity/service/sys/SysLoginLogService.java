package com.campus.activity.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysLoginLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 登录日志服务接口
 */
public interface SysLoginLogService extends IService<SysLoginLog> {

    /**
     * 分页查询登录日志
     */
    IPage<SysLoginLog> getLoginLogPage(Page<SysLoginLog> page, LogQueryDTO query);

    /**
     * 导出登录日志为Excel
     */
    void exportLoginLog(LogQueryDTO query, HttpServletResponse response);

    /**
     * 保存登录日志
     */
    boolean saveLoginLog(SysLoginLog log);
}
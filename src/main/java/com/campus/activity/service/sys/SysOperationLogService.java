package com.campus.activity.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysOperationLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 操作日志服务接口
 */
public interface SysOperationLogService extends IService<SysOperationLog> {

    /**
     * 分页查询操作日志
     */
    IPage<SysOperationLog> getOperationLogPage(Page<SysOperationLog> page, LogQueryDTO query);

    /**
     * 导出操作日志为Excel
     */
    void exportOperationLog(LogQueryDTO query, HttpServletResponse response);

    /**
     * 保存操作日志
     */
    boolean saveOperationLog(SysOperationLog log);
}
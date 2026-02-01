package com.campus.activity.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysOperLog;
import com.campus.activity.service.sys.SysOperLogService;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import java.time.LocalDateTime;

/**
 * 操作日志
 * 操作日志接口
 */
@RestController
@RequestMapping("/api/system/log")
public class SysOperLogController {
    @Resource
    private SysOperLogService sysOperLogService;

    // 分页查询操作日志
    @GetMapping("/list")
    public IPage<SysOperLog> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String operator,
            @RequestParam(required = false) String operType,
            @RequestParam(required = false) String operStatus,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate
    ) {
        Page<SysOperLog> page = new Page<>(pageNum, pageSize);
        return sysOperLogService.lambdaQuery()
                .like(operator != null, SysOperLog::getOperator, operator)
                .eq(operType != null, SysOperLog::getOperType, operType)
                .eq(operStatus != null, SysOperLog::getOperStatus, operStatus)
                .ge(startDate != null, SysOperLog::getOperTime, startDate)
                .le(endDate != null, SysOperLog::getOperTime, endDate)
                .orderByDesc(SysOperLog::getOperTime)
                .page(page);
    }

    // 清空操作日志
    @DeleteMapping("/clear")
    public Boolean clearLog() {
        return sysOperLogService.clearAllLog();
    }
}
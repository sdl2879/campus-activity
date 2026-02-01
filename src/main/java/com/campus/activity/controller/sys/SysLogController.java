package com.campus.activity.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysLoginLog;
import com.campus.activity.entity.sys.SysOperationLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;
import com.campus.activity.service.sys.SysLoginLogService;
import com.campus.activity.service.sys.SysOperationLogService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 日志管理控制器（操作日志/登录日志）
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Resource
    private SysOperationLogService operationLogService;
    @Resource
    private SysLoginLogService loginLogService;

    // ====================== 操作日志接口 ======================
    /**
     * 分页查询操作日志
     */
    @GetMapping("/operation/page")
    public Map<String, Object> getOperationLogPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<SysOperationLog> page = new Page<>(pageNum, pageSize);
            LogQueryDTO query = new LogQueryDTO();
            query.setUserName(userName);
            query.setModule(module);
            query.setOperation(operation);
            query.setStatus(status);
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            IPage<SysOperationLog> logPage = operationLogService.getOperationLogPage(page, query);
            result.put("code", 200);
            result.put("data", logPage.getRecords());
            result.put("total", logPage.getTotal());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询操作日志失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出操作日志
     */
    @GetMapping("/operation/export")
    public void exportOperationLog(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String module,
            @RequestParam(required = false) String operation,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            HttpServletResponse response) {
        try {
            LogQueryDTO query = new LogQueryDTO();
            query.setUserName(userName);
            query.setModule(module);
            query.setOperation(operation);
            query.setStatus(status);
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            operationLogService.exportOperationLog(query, response);
        } catch (Exception e) {
            throw new RuntimeException("导出操作日志失败：" + e.getMessage());
        }
    }

    // ====================== 登录日志接口 ======================
    /**
     * 分页查询登录日志
     */
    @GetMapping("/login/page")
    public Map<String, Object> getLoginLogPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String loginResult,
            @RequestParam(required = false) String loginType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<SysLoginLog> page = new Page<>(pageNum, pageSize);
            LogQueryDTO query = new LogQueryDTO();
            query.setUserName(userName);
            query.setLoginResult(loginResult);
            query.setLoginType(loginType);
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            IPage<SysLoginLog> logPage = loginLogService.getLoginLogPage(page, query);
            result.put("code", 200);
            result.put("data", logPage.getRecords());
            result.put("total", logPage.getTotal());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询登录日志失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出登录日志
     */
    @GetMapping("/login/export")
    public void exportLoginLog(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String loginResult,
            @RequestParam(required = false) String loginType,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime,
            HttpServletResponse response) {
        try {
            LogQueryDTO query = new LogQueryDTO();
            query.setUserName(userName);
            query.setLoginResult(loginResult);
            query.setLoginType(loginType);
            query.setStartTime(startTime);
            query.setEndTime(endTime);

            loginLogService.exportLoginLog(query, response);
        } catch (Exception e) {
            throw new RuntimeException("导出登录日志失败：" + e.getMessage());
        }
    }
}
package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.SysAdmin;
import com.campus.activity.entity.user.SysAdminOperationLog;
import com.campus.activity.service.user.SysAdminService;
import com.campus.activity.service.user.SysAdminOperationLogService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/systemAdmin")
public class SysAdminController {

    private final SysAdminService sysAdminService;
    private final SysAdminOperationLogService operationLogService;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public SysAdminController(SysAdminService sysAdminService, SysAdminOperationLogService operationLogService) {
        this.sysAdminService = sysAdminService;
        this.operationLogService = operationLogService;
    }

    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username,
            String realName,
            Integer status,
            HttpServletRequest request) {

        recordOperationLog(request, "查询", "系统管理员管理",
                String.format("分页查询：pageNum=%d, pageSize=%d, username=%s, realName=%s, status=%d",
                        pageNum, pageSize, username, realName, status));

        Page<SysAdmin> page = new Page<>(pageNum, pageSize);
        IPage<SysAdmin> result = sysAdminService.pageQuery(page, username, realName, status);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());
        data.put("pageNum", pageNum);
        data.put("pageSize", pageSize);
        return ResponseEntity.ok(data);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(
            @Valid @RequestBody SysAdmin sysAdmin,
            HttpServletRequest request) {

        String rawPassword = sysAdmin.getPassword();
        sysAdmin.setPassword(passwordEncoder.encode(rawPassword));
        if (sysAdmin.getStatus() == null) {
            sysAdmin.setStatus(1);
        }
        sysAdmin.setCreateTime(LocalDateTime.now());
        sysAdmin.setUpdateTime(LocalDateTime.now());

        boolean success = sysAdminService.addAdmin(sysAdmin);

        recordOperationLog(request, "新增", "系统管理员管理",
                String.format("新增管理员：username=%s, realName=%s, dept=%s, phone=%s",
                        sysAdmin.getUsername(), sysAdmin.getRealName(),
                        sysAdmin.getDept(), sysAdmin.getPhone()));

        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "新增成功（初始密码：" + rawPassword + "）" : "新增失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(
            @Valid @RequestBody SysAdmin sysAdmin,
            HttpServletRequest request) {

        SysAdmin oldAdmin = sysAdminService.getById(sysAdmin.getId());
        if (oldAdmin == null) {
            return ResponseEntity.ok(Map.of("code", 500, "msg", "管理员不存在"));
        }
        sysAdmin.setUsername(oldAdmin.getUsername());
        sysAdmin.setPassword(oldAdmin.getPassword());
        sysAdmin.setUpdateTime(LocalDateTime.now());

        boolean success = sysAdminService.updateById(sysAdmin);

        recordOperationLog(request, "编辑", "系统管理员管理",
                String.format("编辑管理员：id=%d, realName=%s, status=%d",
                        sysAdmin.getId(), sysAdmin.getRealName(),
                        sysAdmin.getStatus()));

        return ResponseEntity.ok(Map.of(
                "code", success ? 200 : 500,
                "msg", success ? "编辑成功" : "编辑失败"
        ));
    }

    // ========== 内部工具方法 ==========
    private void recordOperationLog(HttpServletRequest request, String operation, String module, String detail) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String ip = getClientIp(request); // 建议用更健壮的 IP 获取方式

        // ✅ 核心修复：通过 operatorId 查询真实姓名，确保 adminName 非空
        String operatorName = "系统";
        if (operatorId != null && operatorId > 0) {
            SysAdmin admin = sysAdminService.getById(operatorId);
            if (admin != null && StringUtils.hasText(admin.getRealName())) {
                operatorName = admin.getRealName();
            }
        }

        SysAdminOperationLog log = new SysAdminOperationLog();
        log.setAdminId(operatorId);
        log.setAdminName(operatorName); // guaranteed NOT NULL
        log.setOperation(operation);
        log.setModule(module);
        log.setIp(ip);
        log.setDetail(detail);
        log.setOperationTime(LocalDateTime.now());

        // ⚠️ 注意：new Thread 不推荐用于生产（无资源控制）
        // 建议改用 @Async + 线程池（需开启 @EnableAsync）
        new Thread(() -> {
            try {
                operationLogService.save(log);
            } catch (Exception e) {
                // 日志记录失败不应影响主流程，但应记录异常
                System.err.println("操作日志记录失败: " + e.getMessage());
                e.printStackTrace();
            }
        }).start();
    }

    // 更健壮的客户端 IP 获取方法
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip != null ? ip.split(",")[0].trim() : "unknown";
    }

    @lombok.Data
    public static class PasswordDTO {
        private Long adminId;
        private String oldPassword;
        private String newPassword;
    }
}
package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.SysAdmin;
import com.campus.activity.service.SysAdminService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/systemAdmin")
public class SysAdminController {

    private final SysAdminService sysAdminService;

    public SysAdminController(SysAdminService sysAdminService) {
        this.sysAdminService = sysAdminService;
    }

    /**
     * 分页查询
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String username,
            String realName,
            Integer status) {
        Page<SysAdmin> page = new Page<>(pageNum, pageSize);
        IPage<SysAdmin> result = sysAdminService.pageQuery(page, username, realName, status);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return ResponseEntity.ok(data);
    }

    /**
     * 新增管理员
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody SysAdmin sysAdmin) {
        boolean success = sysAdminService.addAdmin(sysAdmin);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "新增成功" : "新增失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 编辑管理员
     */
    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(@Valid @RequestBody SysAdmin sysAdmin) {
        boolean success = sysAdminService.updateById(sysAdmin);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "编辑成功" : "编辑失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 切换状态
     */
    @PostMapping("/changeStatus")
    public ResponseEntity<Map<String, Object>> changeStatus(Long id, Integer status) {
        boolean success = sysAdminService.changeStatus(id, status);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "状态修改成功" : "状态修改失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 重置密码
     */
    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, Object>> resetPassword(Long id) {
        boolean success = sysAdminService.resetPassword(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "密码重置成功（默认123456）" : "密码重置失败");
        return ResponseEntity.ok(result);
    }
}
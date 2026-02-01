package com.campus.activity.controller.sys;

import com.campus.activity.entity.sys.SysPermission;
import com.campus.activity.service.sys.SysPermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/sys/permission")
public class SysPermissionController {

    @Resource
    private SysPermissionService sysPermissionService;

    /**
     * 查询所有权限（用于前端权限选择）
     */
    @GetMapping("/listAll")
    public Map<String, Object> listAll() {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysPermission> permList = sysPermissionService.list();
            result.put("code", 200);
            result.put("data", permList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询权限失败：" + e.getMessage());
        }
        return result;
    }
}
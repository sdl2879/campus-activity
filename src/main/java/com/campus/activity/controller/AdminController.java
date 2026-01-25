package com.campus.activity.controller;
import com.campus.activity.entity.Admin;
import com.campus.activity.service.AdminService;
import com.campus.activity.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    /**
     * 修改个人资料
     */
    @PostMapping("/updateProfile")
    public Result<Admin> updateProfile(@RequestBody Admin admin) {
        boolean flag = adminService.updateById(admin);
        if (flag) {
            return Result.success(adminService.getById(admin.getId()));
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * 修改密码
     */
    @PostMapping("/changePassword")
    public Result<String> changePassword(@RequestBody Map<String, String> params) {
        Long adminId = Long.parseLong(params.get("adminId"));
        String oldPwd = params.get("oldPwd");
        String newPwd = params.get("newPwd");

        boolean flag = adminService.changePassword(adminId, oldPwd, newPwd);
        if (flag) {
            return Result.success("密码修改成功");
        } else {
            return Result.error("旧密码错误或修改失败");
        }
    }

    /**
     * 上传头像
     */
    @PostMapping("/upload/avatar")
    public Result<String> uploadAvatar(@RequestParam("file") MultipartFile file) {
        try {
            // 这里实现文件上传逻辑，返回头像URL
            String avatarUrl = adminService.uploadAvatar(file);
            return Result.success(avatarUrl);
        } catch (Exception e) {
            return Result.error("头像上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取当前管理员信息
     */
    @GetMapping("/getCurrentAdmin/{id}")
    public Result<Admin> getCurrentAdmin(@PathVariable Long id) {
        Admin admin = adminService.getById(id);
        // 隐藏密码
        admin.setPassword(null);
        return Result.success(admin);
    }
}
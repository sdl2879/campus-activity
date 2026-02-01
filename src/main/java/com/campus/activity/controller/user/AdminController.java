package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campus.activity.entity.user.SysAdmin;
import com.campus.activity.service.user.SysAdminService;
import com.campus.activity.utils.JwtUtil;
import com.campus.activity.utils.Result;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import jakarta.annotation.Resource;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unchecked", "rawtypes"})
@RestController
@RequestMapping("/api/user/admin")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class AdminController {
    @Resource
    private SysAdminService sysAdminService;

    // 1. 管理员登录（生成AdminToken）- 修复核心问题
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params) {
        try {
            String username = params.get("username");
            String password = params.get("password");

            log.info("=========登录调试=========");
            log.info("接收到的参数: username={}, password={}", username, password);
            log.info("=========================");

            if (username == null || password == null) {
                log.warn("登录失败：用户名或密码为空");
                return Result.error("用户名或密码不能为空");
            }

            SysAdmin sysAdmin = sysAdminService.getOne(new LambdaQueryWrapper<SysAdmin>()
                    .eq(SysAdmin::getUsername, username));
            log.info("根据用户名查询结果：admin={}", sysAdmin == null ? "null" : sysAdmin.getId());

            if (sysAdmin == null) {
                log.warn("登录失败：用户不存在，username={}", username);
                return Result.error("账号或密码错误");
            }
            if (sysAdmin.getPassword() == null || !sysAdmin.getPassword().equals(password)) {
                log.warn("登录失败：密码错误，username={}", username);
                return Result.error("账号或密码错误");
            }

            String adminToken = JwtUtil.generateAdminToken(sysAdmin.getId(), sysAdmin.getUsername());
            log.info("Token生成成功：adminId={}, token={}", sysAdmin.getId(), adminToken);

            Map<String, Object> data = new HashMap<>();
            data.put("adminToken", adminToken);
            data.put("admin", sysAdmin);
            log.info("登录成功，返回数据：{}", data);
            return Result.success(data);

        } catch (Exception e) {
            log.error("登录接口异常", e);
            return Result.error("登录失败：服务器内部错误");
        }
    }

    // 2. 获取当前管理员信息（解析AdminToken，解决类型转换错误）
    @GetMapping("/info")
    public Result<SysAdmin> getAdminInfo(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("获取管理员信息失败：Token不存在或格式错误");
                return Result.error(401, "未登录或Token格式错误");
            }

            String token = authHeader.substring(7);
            Claims claims = JwtUtil.parseAdminToken(token);
            if (claims == null || JwtUtil.isTokenExpired(claims)) {
                log.warn("获取管理员信息失败：Token已过期或无效");
                return Result.error(401, "Token已过期或无效");
            }

            Long adminId = JwtUtil.getAdminIdFromClaims(claims);
            if (adminId == null) {
                log.warn("获取管理员信息失败：Token解析无adminId");
                return Result.error(401, "Token解析失败");
            }

            SysAdmin sysAdmin = sysAdminService.getById(adminId);
            log.info("获取管理员信息成功：adminId={}", adminId);
            return Result.success(sysAdmin);
        } catch (Exception e) {
            log.error("获取管理员信息异常", e);
            return Result.error(401, "登录已过期，请重新登录");
        }
    }

    // 3. 修改个人信息（解析AdminToken）
    @PutMapping("/info")
    public Result<Boolean> updateAdminInfo(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody SysAdmin sysAdmin
    ) {
        try {
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.parseAdminToken(token);
            Long adminId = JwtUtil.getAdminIdFromClaims(claims);

            // 赋值并更新（只更新实体类存在的字段）
            sysAdmin.setId(adminId);
            sysAdmin.setUpdateTime(LocalDateTime.now());
            // 注意：移除了对 avatar/phone/email 等不存在字段的处理
            boolean success = sysAdminService.updateById(sysAdmin);
            log.info("修改管理员信息：adminId={}, success={}", adminId, success);
            return success ? Result.success(true) : Result.error("信息修改失败");
        } catch (Exception e) {
            log.error("修改管理员信息异常", e);
            return Result.error(401, "登录已过期，请重新登录");
        }
    }

    // 4. 修改密码
    @PostMapping("/changePwd")
    public Result<Boolean> changePassword(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, String> params
    ) {
        try {
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.parseAdminToken(token);
            Long adminId = JwtUtil.getAdminIdFromClaims(claims);

            String oldPwd = params.get("oldPwd");
            String newPwd = params.get("newPwd");

            SysAdmin sysAdmin = sysAdminService.getById(adminId);
            if (sysAdmin == null || !sysAdmin.getPassword().equals(oldPwd)) {
                log.warn("修改密码失败：原密码错误，adminId={}", adminId);
                return Result.error("原密码错误");
            }

            sysAdmin.setPassword(newPwd);
            sysAdmin.setUpdateTime(LocalDateTime.now());
            boolean success = sysAdminService.updateById(sysAdmin);
            log.info("修改密码：adminId={}, success={}", adminId, success);
            return Result.success(success);
        } catch (Exception e) {
            log.error("修改密码异常", e);
            return Result.error(401, "登录已过期，请重新登录");
        }
    }

    // 5. 上传头像（注释/删除setAvatar调用，适配无avatar字段的实体类）
    @PostMapping("/uploadAvatar")
    public Result<String> uploadAvatar(
            @RequestHeader("Authorization") String authHeader,
            @RequestParam("file") MultipartFile file
    ) {
        try {
            String token = authHeader.substring(7);
            Claims claims = JwtUtil.parseAdminToken(token);
            Long adminId = JwtUtil.getAdminIdFromClaims(claims);

            // 创建上传目录
            String uploadPath = System.getProperty("user.dir") + "/uploads/avatar/";
            File uploadDir = new File(uploadPath);
            if (!uploadDir.exists()) {
                uploadDir.mkdirs();
            }

            // 保存文件
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            String filePath = uploadPath + fileName;
            file.transferTo(new File(filePath));

            // 核心修复：删除 setAvatar 调用（实体类无该字段）
            // 如需保存头像URL，需先给 sys_admin 表添加 avatar 字段，再更新实体类
            String avatarUrl = "/avatar/" + fileName;
            log.info("上传头像成功（仅保存文件，未更新数据库）：adminId={}, avatarUrl={}", adminId, avatarUrl);

            // 若后续要支持头像，执行以下步骤：
            // 1. ALTER TABLE sys_admin ADD COLUMN avatar varchar(255) COMMENT '头像URL';
            // 2. 在 SysAdmin 实体类添加 private String avatar;
            // 3. 取消下面两行注释：
             SysAdmin sysAdmin = sysAdminService.getById(adminId);
             sysAdmin.setAvatar(avatarUrl);
             sysAdmin.setUpdateTime(LocalDateTime.now());
             sysAdminService.updateById(sysAdmin);

            return Result.success(avatarUrl);
        } catch (IOException e) {
            log.error("头像上传失败（IO异常）", e);
            return Result.error("头像上传失败：" + e.getMessage());
        } catch (Exception e) {
            log.error("头像上传异常", e);
            return Result.error(401, "登录已过期，请重新登录");
        }
    }
}
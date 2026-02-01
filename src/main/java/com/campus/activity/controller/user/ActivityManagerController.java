package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.ActivityManagerAddDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerQueryDTO;
import com.campus.activity.entity.sys.dto.ActivityManagerUpdateDTO;
import com.campus.activity.entity.sys.dto.QualificationAuditDTO;
import com.campus.activity.entity.user.ActivityManager;
import com.campus.activity.service.user.ActivityManagerService;
import com.campus.activity.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/activityManager")
public class ActivityManagerController {

    private final ActivityManagerService activityManagerService;

    public ActivityManagerController(ActivityManagerService activityManagerService) {
        this.activityManagerService = activityManagerService;
    }

    @PostMapping("/page")
    public Result<Map<String, Object>> getActivityManagerPage(@RequestBody ActivityManagerQueryDTO query) {
        IPage<ActivityManager> page = activityManagerService.getActivityManagerPage(query);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        return Result.success(data);
    }

    @GetMapping("/{id}")
    public Result<ActivityManager> getActivityManagerDetail(@PathVariable Long id) {
        ActivityManager manager = activityManagerService.getActivityManagerById(id);
        return Result.success(manager);
    }

    @PostMapping("/add")
    public Result<String> addActivityManager(
            @Valid @RequestBody ActivityManagerAddDTO addDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = activityManagerService.addActivityManager(addDTO, operatorId, operatorName, ip);
        if (success) {
            return Result.success("新增成功（初始密码：" + addDTO.getPassword() + "，资质状态：待审核）");
        } else {
            return Result.error("新增失败");
        }
    }

    @PostMapping("/update")
    public Result<String> updateActivityManager(
            @Valid @RequestBody ActivityManagerUpdateDTO updateDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = activityManagerService.updateActivityManager(updateDTO, operatorId, operatorName, ip);
        if (success) {
            return Result.success("编辑成功");
        } else {
            return Result.error("编辑失败");
        }
    }

    // ✅ 统一改为 @RequestBody
    @PostMapping("/updateStatus")
    public Result<String> updateStatus(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        Long id = ((Number) payload.get("id")).longValue();
        Integer status = (Integer) payload.get("status");

        Long operatorId = (Long) request.getAttribute("adminId");
        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = activityManagerService.updateStatus(id, status, operatorId, operatorName, ip);
        if (success) {
            String msg = status == 1 ? "启用成功" : "禁用成功";
            return Result.success(msg);
        } else {
            return Result.error("状态修改失败");
        }
    }

    @PostMapping("/auditQualification")
    public Result<String> auditQualification(
            @Valid @RequestBody QualificationAuditDTO auditDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = activityManagerService.auditQualification(auditDTO, operatorId, operatorName, ip);
        if (success) {
            String msg = auditDTO.getQualificationStatus() == 1 ? "审核通过成功" : "审核驳回成功";
            return Result.success(msg);
        } else {
            return Result.error("审核操作失败");
        }
    }

    // ✅ 统一改为 @RequestBody
    @PostMapping("/resetPassword")
    public Result<String> resetPassword(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        Long id = ((Number) payload.get("id")).longValue();

        Long operatorId = (Long) request.getAttribute("adminId");
        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = activityManagerService.resetPassword(id, operatorId, operatorName, ip);
        if (success) {
            return Result.success("密码重置成功（默认123456）");
        } else {
            return Result.error("密码重置失败");
        }
    }

    @PostMapping("/export")
    public Result<List<ActivityManager>> exportActivityManagerList(@RequestBody ActivityManagerQueryDTO query) {
        List<ActivityManager> list = activityManagerService.exportActivityManagerList(query);
        return Result.success(list);
    }

    @GetMapping("/listByDeptId")
    public Result<List<ActivityManager>> listByDeptId(
            @RequestParam Long deptId,
            @RequestParam(required = false) Integer qualificationStatus) {
        List<ActivityManager> list = activityManagerService.listByDeptId(deptId, qualificationStatus);
        return Result.success(list);
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteActivityManager(@PathVariable Long id) {
        Boolean success = activityManagerService.deleteActivityManager(id);
        if (success) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }
}
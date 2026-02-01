package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.DeptAdminAddDTO;
import com.campus.activity.entity.sys.dto.DeptAdminQueryDTO;
import com.campus.activity.entity.sys.dto.DeptAdminUpdateDTO;
import com.campus.activity.entity.user.DeptAdmin;
import com.campus.activity.service.user.DeptAdminService;
import com.campus.activity.utils.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.security.access.prepost.PreAuthorize; // ← 新增导入
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin/deptAdmin")
public class DeptAdminController {

    private final DeptAdminService deptAdminService;

    public DeptAdminController(DeptAdminService deptAdminService) {
        this.deptAdminService = deptAdminService;
    }

    @PostMapping("/page")
    public Result<IPage<DeptAdmin>> getDeptAdminPage(@RequestBody DeptAdminQueryDTO query) {
        IPage<DeptAdmin> page = deptAdminService.getDeptAdminPage(query);
        return Result.success(page);
    }

    @GetMapping("/{id}")
    public Result<DeptAdmin> getDeptAdminDetail(@PathVariable Long id) {
        DeptAdmin deptAdmin = deptAdminService.getDeptAdminById(id);
        return Result.success(deptAdmin);
    }

    @PostMapping("/add")
    public Result<String> addDeptAdmin(
            @Valid @RequestBody DeptAdminAddDTO addDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }

        Boolean success = deptAdminService.addDeptAdmin(addDTO, operatorId, operatorName, ip);
        if (success) {
            return Result.success("新增成功（初始密码：" + addDTO.getPassword() + "）");
        } else {
            return Result.error("新增失败");
        }
    }

    @PostMapping("/update")
    public Result<String> updateDeptAdmin(
            @Valid @RequestBody DeptAdminUpdateDTO updateDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }

        Boolean success = deptAdminService.updateDeptAdmin(updateDTO, operatorId, operatorName, ip);
        if (success) {
            return Result.success("编辑成功");
        } else {
            return Result.error("编辑失败");
        }
    }

    // ✅ 新增 @PreAuthorize
    @PostMapping("/updateStatus")
    @PreAuthorize("hasAuthority('admin:deptAdmin:updateStatus')")
    public Result<String> updateStatus(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        Long id = ((Number) payload.get("id")).longValue();
        Integer status = (Integer) payload.get("status");

        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }

        Boolean success = deptAdminService.updateStatus(id, status, operatorId, operatorName, ip);
        if (success) {
            String msg = status == 1 ? "启用成功" : "禁用成功";
            return Result.success(msg);
        } else {
            return Result.error("状态修改失败");
        }
    }

    // ✅ 新增 @PreAuthorize
    @PostMapping("/resetPassword")
    @PreAuthorize("hasAuthority('admin:deptAdmin:resetPassword')")
    public Result<String> resetPassword(@RequestBody Map<String, Object> payload, HttpServletRequest request) {
        Long id = ((Number) payload.get("id")).longValue();

        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        if (operatorId == null) {
            return Result.error(401, "未登录或会话已过期");
        }

        Boolean success = deptAdminService.resetPassword(id, operatorId, operatorName, ip);
        if (success) {
            return Result.success("密码重置成功（默认123456）");
        } else {
            return Result.error("密码重置失败");
        }
    }

    @PostMapping("/export")
    public List<DeptAdmin> exportDeptAdminList(@RequestBody DeptAdminQueryDTO query) {
        return deptAdminService.exportDeptAdminList(query);
    }

    @GetMapping("/listByDeptId")
    public Result<List<DeptAdmin>> listByDeptId(@RequestParam Long deptId) {
        List<DeptAdmin> list = deptAdminService.listByDeptId(deptId);
        return Result.success(list);
    }
}
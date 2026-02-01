package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.ScoreAdjustDTO;
import com.campus.activity.entity.sys.dto.StudentAddDTO;
import com.campus.activity.entity.sys.dto.StudentQueryDTO;
import com.campus.activity.entity.sys.dto.StudentUpdateDTO;
import com.campus.activity.entity.user.Student;
import com.campus.activity.entity.user.StudentScoreLog;
import com.campus.activity.service.user.StudentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学生管理控制器（适配 grade VARCHAR(20) 格式，如 "2023级"）
 */
@RestController
@RequestMapping("/api/admin/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 分页查询学生列表（支持 graduationStatus 筛选）
     * - graduationStatus: null=全部, 0=未毕业, 1=已毕业
     * - 判断逻辑：2026年当前时间，4年制 → grade <= "2022级" 视为已毕业
     */
    @PostMapping("/page")
    public ResponseEntity<Map<String, Object>> getStudentPage(@RequestBody StudentQueryDTO query) {
        IPage<Student> page = studentService.getStudentPage(query);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        data.put("pageNum", query.getPageNum());
        data.put("pageSize", query.getPageSize());
        return ResponseEntity.ok(data);
    }

    // ========== 其他接口保持不变 ==========

    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentDetail(@PathVariable Long id) {
        Student student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/getByNo")
    public ResponseEntity<Student> getStudentByNo(@RequestParam String studentNo) {
        Student student = studentService.getStudentByNo(studentNo);
        return ResponseEntity.ok(student);
    }

    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addStudent(
            @Valid @RequestBody StudentAddDTO addDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = studentService.addStudent(addDTO, operatorId, operatorName, ip);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "新增成功（初始密码：" + addDTO.getPassword() + "）" : "新增失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, Object>> updateStudent(
            @Valid @RequestBody StudentUpdateDTO updateDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = studentService.updateStudent(updateDTO, operatorId, operatorName, ip);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "编辑成功" : "编辑失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/updateStatus")
    public ResponseEntity<Map<String, Object>> updateStatus(
            @RequestParam Long id,
            @RequestParam Integer status,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = studentService.updateStatus(id, status, operatorId, operatorName, ip);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? (status == 1 ? "启用成功" : "禁用成功") : "状态修改失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<Map<String, Object>> resetPassword(
            @RequestParam Long id,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = studentService.resetPassword(id, operatorId, operatorName, ip);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "密码重置成功（默认123456）" : "密码重置失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/adjustScore")
    public ResponseEntity<Map<String, Object>> adjustScore(
            @Valid @RequestBody ScoreAdjustDTO adjustDTO,
            HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("adminId");
        String operatorName = (String) request.getAttribute("adminName");
        String ip = request.getRemoteAddr();

        Boolean success = studentService.adjustScore(adjustDTO, operatorId, operatorName, ip);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "分值调整成功" : "分值调整失败");
        return ResponseEntity.ok(result);
    }

    @PostMapping("/export")
    public ResponseEntity<List<Student>> exportStudentList(@RequestBody StudentQueryDTO query) {
        List<Student> list = studentService.exportStudentList(query);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/listByDeptId")
    public ResponseEntity<List<Student>> listByDeptId(
            @RequestParam Long deptId,
            @RequestParam(required = false) Integer status) {
        List<Student> list = studentService.listByDeptId(deptId, status);
        return ResponseEntity.ok(list);
    }

    @GetMapping("/scoreLog/page")
    public ResponseEntity<Map<String, Object>> getScoreLogPage(
            @RequestParam Long studentId,
            @RequestParam(required = false) Long activityId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        IPage<StudentScoreLog> page = studentService.getScoreLogPage(studentId, activityId, pageNum, pageSize);
        Map<String, Object> data = new HashMap<>();
        data.put("list", page.getRecords());
        data.put("total", page.getTotal());
        return ResponseEntity.ok(data);
    }
}
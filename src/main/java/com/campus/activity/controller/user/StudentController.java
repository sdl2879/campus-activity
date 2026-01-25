package com.campus.activity.controller.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.Student;
import com.campus.activity.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * 分页查询
     */
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            String studentNo,
            String studentName,
            Long deptId,
            String subjectType,
            String grade,
            Integer isGraduate) {
        Page<Student> page = new Page<>(pageNum, pageSize);
        IPage<Student> result = studentService.pageQuery(page, studentNo, studentName, deptId,
                subjectType, grade, isGraduate);

        Map<String, Object> data = new HashMap<>();
        data.put("list", result.getRecords());
        data.put("total", result.getTotal());

        return ResponseEntity.ok(data);
    }

    /**
     * 新增学生
     */
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> add(@Valid @RequestBody Student student) {
        boolean success = studentService.addStudent(student);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "新增成功" : "新增失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 编辑学生
     */
    @PostMapping("/edit")
    public ResponseEntity<Map<String, Object>> edit(@Valid @RequestBody Student student) {
        boolean success = studentService.updateStudent(student);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "编辑成功" : "编辑失败");
        return ResponseEntity.ok(result);
    }

    /**
     * 删除学生
     */
    @PostMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> delete(@PathVariable Long id) {
        boolean success = studentService.removeById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("msg", success ? "删除成功" : "删除失败");
        return ResponseEntity.ok(result);
    }
}

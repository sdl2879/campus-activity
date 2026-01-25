package com.campus.activity.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.user.Student;

public interface StudentService extends IService<Student> {
    /**
     * 分页查询学生
     */
    IPage<Student> pageQuery(Page<Student> page, String studentNo, String studentName, Long deptId,
                             String subjectType, String grade, Integer isGraduate);

    /**
     * 新增学生（自动处理毕业状态：非大四强制设为0）
     */
    boolean addStudent(Student student);

    /**
     * 修改学生（自动处理毕业状态）
     */
    boolean updateStudent(Student student);
}
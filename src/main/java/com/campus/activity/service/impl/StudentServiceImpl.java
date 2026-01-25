package com.campus.activity.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.user.Student;
import com.campus.activity.Mapper.user.StudentMapper;
import com.campus.activity.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class StudentServiceImpl extends ServiceImpl<StudentMapper, Student> implements StudentService {

    @Override
    public IPage<Student> pageQuery(Page<Student> page, String studentNo, String studentName, Long deptId,
                                    String subjectType, String grade, Integer isGraduate) {
        LambdaQueryWrapper<Student> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(studentNo)) {
            wrapper.like(Student::getStudentNo, studentNo);
        }
        if (StringUtils.hasText(studentName)) {
            wrapper.like(Student::getStudentName, studentName);
        }
        if (deptId != null) {
            wrapper.eq(Student::getDeptId, deptId);
        }
        if (StringUtils.hasText(subjectType)) {
            wrapper.eq(Student::getSubjectType, subjectType);
        }
        if (StringUtils.hasText(grade)) {
            wrapper.eq(Student::getGrade, grade);
        }
        if (isGraduate != null) {
            wrapper.eq(Student::getIsGraduate, isGraduate);
        }
        wrapper.orderByDesc(Student::getCreateTime);
        return baseMapper.selectPage(page, wrapper);
    }

    @Override
    public boolean addStudent(Student student) {
        // 非大四学生强制设为未毕业
        if (!"SENIOR".equals(student.getGrade())) {
            student.setIsGraduate(0);
        }
        return save(student);
    }

    @Override
    public boolean updateStudent(Student student) {
        // 非大四学生强制设为未毕业
        if (!"SENIOR".equals(student.getGrade())) {
            student.setIsGraduate(0);
        }
        return updateById(student);
    }
}
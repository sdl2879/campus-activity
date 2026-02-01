package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.StudentScoreLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentScoreLogMapper extends BaseMapper<StudentScoreLog> {
    // 分页查询学生分值变动记录
    IPage<StudentScoreLog> selectScoreLogPage(Page<StudentScoreLog> page,
                                              @Param("studentId") Long studentId,
                                              @Param("activityId") Long activityId);

    // 查询学生总分值变动记录
    List<StudentScoreLog> selectByStudentId(@Param("studentId") Long studentId);
}
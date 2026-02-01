package com.campus.activity.service.user;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.campus.activity.entity.sys.dto.ScoreAdjustDTO;
import com.campus.activity.entity.sys.dto.StudentAddDTO;
import com.campus.activity.entity.sys.dto.StudentQueryDTO;
import com.campus.activity.entity.sys.dto.StudentUpdateDTO;
import com.campus.activity.entity.user.Student;
import com.campus.activity.entity.user.StudentScoreLog;

import java.util.List;

/**
 * 学生管理服务接口（第二课堂核心模块）
 * 提供学生全生命周期管理及分值操作能力
 */
public interface StudentService {

    /**
     * 分页查询学生列表（支持多条件筛选）
     */
    IPage<Student> getStudentPage(StudentQueryDTO query);

    /**
     * 根据ID查询学生详情
     */
    Student getStudentById(Long id);

    /**
     * 根据学号查询学生
     */
    Student getStudentByNo(String studentNo);

    /**
     * 新增学生（自动设置初始密码、总分=0、状态=启用）
     * @param addDTO 新增参数
     * @param operatorId 操作人ID（管理员）
     * @param operatorName 操作人姓名
     * @param ip 操作IP
     * @return 成功返回 true
     */
    Boolean addStudent(StudentAddDTO addDTO, Long operatorId, String operatorName, String ip);

    /**
     * 编辑学生基本信息（不含密码）
     */
    Boolean updateStudent(StudentUpdateDTO updateDTO, Long operatorId, String operatorName, String ip);

    /**
     * 启用/禁用学生账号
     */
    Boolean updateStatus(Long id, Integer status, Long operatorId, String operatorName, String ip);

    /**
     * 重置学生密码为默认值 "123456"
     */
    Boolean resetPassword(Long id, Long operatorId, String operatorName, String ip);

    /**
     * 调整学生第二课堂分值（支持加分/减分，记录日志）
     * @param adjustDTO 调整参数（含学生ID、分值、活动ID、类型、备注等）
     * @return 成功返回 true
     */
    Boolean adjustScore(ScoreAdjustDTO adjustDTO, Long operatorId, String operatorName, String ip);

    /**
     * 导出全部符合条件的学生列表（不分页）
     */
    List<Student> exportStudentList(StudentQueryDTO query);

    /**
     * 根据院系ID查询学生（可选状态过滤）
     */
    List<Student> listByDeptId(Long deptId, Integer status);

    /**
     * 分页查询学生分值变动记录
     */
    IPage<StudentScoreLog> getScoreLogPage(Long studentId, Long activityId, Integer pageNum, Integer pageSize);
}
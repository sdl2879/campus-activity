package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.dto.StudentQueryDTO;
import com.campus.activity.entity.user.Student;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * 学生数据访问接口
 * 继承 MyBatis-Plus BaseMapper，提供基础 CRUD，并扩展自定义查询方法
 */
public interface StudentMapper extends BaseMapper<Student> {

    /**
     * 分页多条件查询学生列表
     * @param page 分页对象
     * @param query 查询条件（支持学号、姓名、院系、年级、分值范围等）
     * @return 分页结果
     */
    IPage<Student> selectStudentPage(Page<Student> page, @Param("query") StudentQueryDTO query);

    /**
     * 根据学号精确查询学生（用于唯一性校验或登录）
     * @param studentNo 学号
     * @return 学生实体，不存在则返回 null
     */
    Student selectByStudentNo(@Param("studentNo") String studentNo);

    /**
     * 根据院系ID查询学生列表（可选按账号状态过滤）
     * @param deptId 院系ID
     * @param status 账号状态（1-启用，0-禁用，null 表示不限）
     * @return 学生列表
     */
    List<Student> selectByDeptId(@Param("deptId") Long deptId, @Param("status") Integer status);

    /**
     * 原子更新学生第二课堂总分值（使用数据库 + 操作）
     * @param id 学生ID
     * @param score 调整分值（可正可负）
     * @return 影响行数（通常为1）
     */
    int updateTotalScore(@Param("id") Long id, @Param("score") BigDecimal score);
}
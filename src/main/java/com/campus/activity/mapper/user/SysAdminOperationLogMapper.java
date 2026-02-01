package com.campus.activity.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.user.SysAdminOperationLog;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDateTime;

/**
 * 操作日志 Mapper —— 已修复：
 * 1. selectLogPage 支持完整查询参数
 * 2. deleteByTimeRange 返回 int（删除行数）
 */
@Mapper
public interface SysAdminOperationLogMapper extends BaseMapper<SysAdminOperationLog> {

    /**
     * 分页查询操作日志（支持多条件）
     * @param page 分页对象
     * @param adminId 管理员ID（可选）
     * @param module 模块（可选）
     * @param keyword 关键词（操作内容模糊搜索，可选）
     * @param startTime 起始时间（可选）
     * @param endTime 结束时间（可选）
     * @return 分页结果
     */
    IPage<SysAdminOperationLog> selectLogPage(
            Page<SysAdminOperationLog> page,
            @Param("adminId") Long adminId,
            @Param("module") String module,
            @Param("keyword") String keyword,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime
    );

    /**
     * 根据时间范围批量删除日志，并返回删除行数
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return 删除的记录数（int）
     */
    int deleteByTimeRange( // 🔥 返回类型从 void 改为 int
                           @Param("startTime") LocalDateTime startTime,
                           @Param("endTime") LocalDateTime endTime
    );

    /**
     * 根据管理员ID查询所有操作日志
     */
    java.util.List<SysAdminOperationLog> selectByAdminId(@Param("adminId") Long adminId);
}
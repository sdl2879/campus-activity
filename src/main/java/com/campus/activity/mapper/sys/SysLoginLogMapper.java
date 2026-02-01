package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysLoginLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 登录日志Mapper（必须放在 com.campus.activity.mapper.sys 包下）
 */
@Mapper // 关键：添加 @Mapper 注解，让 MyBatis-Plus 扫描到
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    /**
     * 分页查询登录日志（多条件筛选）
     */
    IPage<SysLoginLog> selectLoginLogPage(Page<SysLoginLog> page, @Param("query") LogQueryDTO query);

    /**
     * 导出登录日志（多条件筛选）
     */
    List<SysLoginLog> selectLoginLogExport(@Param("query") LogQueryDTO query);
}
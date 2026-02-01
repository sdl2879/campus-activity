package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysOperationLog;
import com.campus.activity.entity.sys.dto.LogQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 操作日志Mapper
 */
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    /**
     * 分页查询操作日志（多条件筛选）
     */
    IPage<SysOperationLog> selectOperationLogPage(Page<SysOperationLog> page, @Param("query") LogQueryDTO query);

    /**
     * 导出操作日志（多条件筛选）
     */
    List<SysOperationLog> selectOperationLogExport(@Param("query") LogQueryDTO query);
}
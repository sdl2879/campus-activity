package com.campus.activity.mapper.sys;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.entity.sys.dto.ConfigQueryDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统配置Mapper
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 分页查询配置（支持按类型/配置键筛选）
     */
    IPage<SysConfig> selectConfigPage(Page<SysConfig> page, @Param("query") ConfigQueryDTO query);

    /**
     * 按配置类型查询配置列表
     */
    List<SysConfig> selectByConfigType(@Param("configType") Integer configType);

    /**
     * 按配置键查询（唯一）
     */
    SysConfig selectByConfigKey(@Param("configKey") String configKey);
}
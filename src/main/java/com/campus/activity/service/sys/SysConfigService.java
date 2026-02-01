package com.campus.activity.service.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.entity.sys.dto.ConfigQueryDTO;

import java.math.BigDecimal;

/**
 * 系统配置服务接口
 */
public interface SysConfigService extends IService<SysConfig> {

    /**
     * 分页查询配置
     */
    IPage<SysConfig> getConfigPage(Page<SysConfig> page, ConfigQueryDTO query);

    /**
     * 保存/更新配置（含参数校验）
     */
    boolean saveOrUpdateConfig(SysConfig config);

    /**
     * 按配置类型查询配置列表
     */
    java.util.List<SysConfig> getConfigByType(Integer configType);

    /**
     * 按配置键查询配置值
     */
    String getConfigValueByKey(String configKey);
}
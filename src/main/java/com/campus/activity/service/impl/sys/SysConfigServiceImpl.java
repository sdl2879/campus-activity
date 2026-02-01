package com.campus.activity.service.impl.sys;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.entity.sys.dto.ConfigQueryDTO;
import com.campus.activity.mapper.sys.SysConfigMapper;
import com.campus.activity.service.sys.SysConfigService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import jakarta.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

/**
 * 系统配置服务实现类（含参数校验）
 */
@Service
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper, SysConfig> implements SysConfigService {

    @Resource
    private SysConfigMapper sysConfigMapper;

    @Override
    public IPage<SysConfig> getConfigPage(Page<SysConfig> page, ConfigQueryDTO query) {
        return sysConfigMapper.selectConfigPage(page, query);
    }

    @Override
    public boolean saveOrUpdateConfig(SysConfig config) {
        // 1. 基础参数校验
        if (!StringUtils.hasText(config.getConfigKey())) {
            throw new RuntimeException("配置键不能为空");
        }
        if (config.getConfigType() == null || (config.getConfigType() < 1 || config.getConfigType() > 3)) {
            throw new RuntimeException("配置类型必须为1（基础）、2（安全）、3（算法）");
        }

        // 2. 按配置类型做专项校验
        validateConfigByType(config);

        // 3. 校验配置键是否重复（新增时）
        if (config.getId() == null) {
            LambdaQueryWrapper<SysConfig> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysConfig::getConfigKey, config.getConfigKey());
            if (sysConfigMapper.selectCount(wrapper) > 0) {
                throw new RuntimeException("配置键已存在，不可重复创建");
            }
        }

        // 4. 算法配置自动填充权重
        if (config.getConfigType() == 3 && StringUtils.hasText(config.getConfigValue())) {
            try {
                config.setWeight(new BigDecimal(config.getConfigValue()));
            } catch (Exception e) {
                throw new RuntimeException("算法配置值必须为数字（权重）");
            }
        }

        return saveOrUpdate(config);
    }

    /**
     * 按配置类型做专项参数校验
     */
    private void validateConfigByType(SysConfig config) {
        String configKey = config.getConfigKey();
        String configValue = config.getConfigValue();

        // 安全配置校验
        if (config.getConfigType() == 2) {
            if ("login_fail_limit".equals(configKey)) {
                // 登录失败次数必须为正整数
                if (!StringUtils.hasText(configValue) || !configValue.matches("^[1-9]\\d*$")) {
                    throw new RuntimeException("登录失败限制次数必须为正整数");
                }
            } else if ("jwt_expire".equals(configKey)) {
                // JWT过期时间必须为正整数
                if (!StringUtils.hasText(configValue) || !configValue.matches("^[1-9]\\d*$")) {
                    throw new RuntimeException("JWT过期时间必须为正整数（分钟）");
                }
            } else if ("password_strategy".equals(configKey)) {
                // 密码规则必须为非空字符串
                if (!StringUtils.hasText(configValue)) {
                    throw new RuntimeException("密码复杂度规则不能为空");
                }
            }
        }

        // 算法配置校验
        if (config.getConfigType() == 3) {
            if ("collaborative_filter_switch".equals(configKey)) {
                // 开关必须为1或0
                if (!"0".equals(configValue) && !"1".equals(configValue)) {
                    throw new RuntimeException("协同过滤开关值必须为1（开启）或0（关闭）");
                }
            } else if ("content_based_weight".equals(configKey) || "collaborative_filter_weight".equals(configKey)) {
                // 权重必须为0-100的数字
                try {
                    BigDecimal weight = new BigDecimal(configValue);
                    if (weight.compareTo(BigDecimal.ZERO) < 0 || weight.compareTo(new BigDecimal("100")) > 0) {
                        throw new RuntimeException("推荐算法权重必须在0-100之间");
                    }
                } catch (Exception e) {
                    throw new RuntimeException("推荐算法权重必须为数字（0-100）");
                }
            }
        }
    }

    @Override
    public List<SysConfig> getConfigByType(Integer configType) {
        return sysConfigMapper.selectByConfigType(configType);
    }

    @Override
    public String getConfigValueByKey(String configKey) {
        SysConfig config = sysConfigMapper.selectByConfigKey(configKey);
        return config != null ? config.getConfigValue() : null;
    }
}
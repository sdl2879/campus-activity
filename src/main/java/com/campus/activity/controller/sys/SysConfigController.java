package com.campus.activity.controller.sys;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.activity.entity.sys.SysConfig;
import com.campus.activity.entity.sys.dto.ConfigQueryDTO;
import com.campus.activity.service.sys.SysConfigService;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统配置控制器（基础/安全/算法配置）
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Resource
    private SysConfigService sysConfigService;

    /**
     * 分页查询配置（支持按类型/配置键筛选）
     */
    @GetMapping("/page")
    public Map<String, Object> getConfigPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Integer configType,
            @RequestParam(required = false) String configKey) {
        Map<String, Object> result = new HashMap<>();
        try {
            Page<SysConfig> page = new Page<>(pageNum, pageSize);
            ConfigQueryDTO query = new ConfigQueryDTO();
            query.setConfigType(configType);
            query.setConfigKey(configKey);

            IPage<SysConfig> configPage = sysConfigService.getConfigPage(page, query);
            result.put("code", 200);
            result.put("data", configPage.getRecords());
            result.put("total", configPage.getTotal());
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 按类型查询配置列表
     */
    @GetMapping("/type/{configType}")
    public Map<String, Object> getConfigByType(@PathVariable Integer configType) {
        Map<String, Object> result = new HashMap<>();
        try {
            List<SysConfig> configList = sysConfigService.getConfigByType(configType);
            result.put("code", 200);
            result.put("data", configList);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询配置失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 按配置键查询配置值
     */
    @GetMapping("/key/{configKey}")
    public Map<String, Object> getConfigValueByKey(@PathVariable String configKey) {
        Map<String, Object> result = new HashMap<>();
        try {
            String configValue = sysConfigService.getConfigValueByKey(configKey);
            result.put("code", 200);
            result.put("data", configValue);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "查询配置值失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 保存/更新配置
     */
    @PostMapping("/saveOrUpdate")
    public Map<String, Object> saveOrUpdateConfig(@RequestBody SysConfig config) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = sysConfigService.saveOrUpdateConfig(config);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "配置保存成功" : "配置保存失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "配置保存失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除配置
     */
    @DeleteMapping("/{id}")
    public Map<String, Object> deleteConfig(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        try {
            boolean success = sysConfigService.removeById(id);
            result.put("code", success ? 200 : 500);
            result.put("msg", success ? "配置删除成功" : "配置删除失败");
        } catch (Exception e) {
            result.put("code", 500);
            result.put("msg", "配置删除失败：" + e.getMessage());
        }
        return result;
    }
}
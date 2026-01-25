package com.campus.activity.entity.sys;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
@TableName("sys_oper_log")
public class SysOperLog {
    @TableId(type = IdType.AUTO)
    private Long id;                 // 日志ID
    private String operator;         // 操作人
    private LocalDateTime operTime;  // 操作时间
    private String operType;         // 操作类型（add/edit/delete等）
    private String operModule;       // 操作模块
    private String operContent;      // 操作内容
    private String operIp;           // 操作IP
    private String operStatus;       // 操作状态（success/fail）
    private String errorMsg;         // 错误信息
}
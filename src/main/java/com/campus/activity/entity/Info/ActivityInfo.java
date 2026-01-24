package com.campus.activity.entity.Info;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("activity_info")
public class ActivityInfo {
    @TableId(type = IdType.AUTO)
    private Long activityId;      // 活动ID
    private String title;         // 活动名称
    private String type;          // 活动类型
    private Integer score;        // 活动分值
    private String deptName;      // 负责院系
    private String status;        // 活动状态
    private LocalDateTime startTime; // 开始时间
    private LocalDateTime endTime;   // 结束时间
    private String location;      // 活动地点
    private String applyCondition; // 报名条件
    private String content;       // 活动详情
    private String fileList;      // 附件列表（JSON）
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime; // 创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime; // 更新时间
    @TableLogic
    private Integer deleted;      // 逻辑删除

    // 非数据库字段
    @TableField(exist = false)
    private List<String> fileListObj; // 附件列表对象
    @TableField(exist = false)
    private String typeName;      // 活动类型名称
    @TableField(exist = false)
    private String statusName;    // 活动状态名称
}
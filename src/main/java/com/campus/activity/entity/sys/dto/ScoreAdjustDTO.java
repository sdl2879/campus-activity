package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

@Data
public class ScoreAdjustDTO {
    @NotNull(message = "学生ID不能为空")
    private Long studentId;         // 学生ID

    @NotNull(message = "调整分值不能为空")
    private BigDecimal score;       // 调整分值（正数加，负数减）

    @NotNull(message = "变动类型不能为空")
    private Integer type;           // 变动类型：1-活动参与，2-活动获奖，3-管理员调整，4-其他

    @Size(max = 500, message = "变动说明不能超过500字符")
    private String remark;          // 变动说明

    private Long activityId;        // 关联活动ID（可选）
    private String activityName;    // 关联活动名称（可选）
}
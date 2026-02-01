package com.campus.activity.entity.user;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("student_score_log")
public class StudentScoreLog {
    @TableId(type = IdType.AUTO)
    private Long id;                // 主键ID
    private Long studentId;         // 学生ID
    private String studentNo;       // 学号（冗余）
    private String studentName;     // 学生姓名（冗余）
    private Long activityId;        // 关联活动ID
    private String activityName;    // 关联活动名称
    private BigDecimal score;       // 变动分值
    private Integer type;           // 变动类型：1-活动参与，2-活动获奖，3-管理员调整，4-其他
    private String remark;          // 变动说明
    private Long operatorId;        // 操作人ID
    private String operatorName;    // 操作人姓名
    private LocalDateTime createTime; // 创建时间
}
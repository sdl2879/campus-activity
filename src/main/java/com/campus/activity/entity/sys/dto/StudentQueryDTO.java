package com.campus.activity.entity.sys.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 学生分页查询条件 DTO
 * 支持多维度筛选（学号、姓名、院系、年级、分值范围、时间范围等）
 * 注意：不包含 graduationStatus 字段，但 Service 层可通过 grade 推导毕业状态
 */
@Data
public class StudentQueryDTO {

    private String studentNo;       // 学号（支持模糊或精准匹配）

    private String name;            // 姓名（模糊查询）

    private Long deptId;            // 院系ID（精确匹配）

    private Long majorId;           // 专业ID（精确匹配）

    private String grade;           // 年级（如 "2023级"，精确匹配）

    private String className;       // 班级名称（精确匹配）

    private Integer gender;         // 性别筛选（0/1/2）

    private Integer status;         // 账号状态（0-禁用，1-启用）

    private BigDecimal minScore;    // 第二课堂最低分值（含）

    private BigDecimal maxScore;    // 第二课堂最高分值（含）

    private LocalDateTime startTime; // 创建时间起始（用于 createTime >= startTime）

    private LocalDateTime endTime;   // 创建时间结束（用于 createTime <= endTime）

    private Integer pageNum = 1;    // 当前页码，默认第1页

    private Integer pageSize = 10;  // 每页记录数，默认10条
}
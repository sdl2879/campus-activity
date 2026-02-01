package com.campus.activity.entity.sys.dto;

import lombok.Data;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Data
public class QualificationAuditDTO {
    @NotNull(message = "活动负责人ID不能为空")
    private Long id;                // 主键ID

    @NotNull(message = "审核结果不能为空")
    private Integer qualificationStatus; // 审核结果（1-已通过，2-已驳回）

    @Size(max = 500, message = "驳回原因不能超过500字符")
    private String rejectReason;    // 驳回原因（仅驳回时必填）
}
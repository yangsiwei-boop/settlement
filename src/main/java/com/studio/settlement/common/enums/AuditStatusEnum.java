package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 审核状态枚举
 */
@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    AUDITED(1, "已审核"),
    NON_AUDIT(2, "未审核");

    private Integer code;

    private String desc;

}

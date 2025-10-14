package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 复核状态枚举
 */
@Getter
@AllArgsConstructor
public enum ReviewStatusEnum {

    REVIEWED(1, "已复核"),
    NON_REVIEW(2, "未复核");

    private Integer code;

    private String desc;

}

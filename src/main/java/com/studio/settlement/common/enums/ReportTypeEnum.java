package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 填报类型枚举
 */
@Getter
@AllArgsConstructor
public enum ReportTypeEnum {

    SUPPLEMENTARY_RECORDING(1, "补录"),
    CORRECTION(2, "修正");

    private Integer code;

    private String desc;

}

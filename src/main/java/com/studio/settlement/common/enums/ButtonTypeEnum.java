package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 按钮类型枚举
 */
@Getter
@AllArgsConstructor
public enum ButtonTypeEnum {

    ADD(1, "新增"),
    REMOVE(2, "删除"),
    EDIT(3, "编辑"),
    QUERY(4, "查看");

    private Integer code;

    private String desc;

    public static String getButtonTypeDesc(Integer code) {
        for (ButtonTypeEnum value : ButtonTypeEnum.values()) {
            if (value.getCode().intValue() == code.intValue()) {
                return value.getDesc();
            }
        }

        return null;
    }

}

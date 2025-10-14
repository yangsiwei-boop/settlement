package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 菜单类型枚举
 */
@Getter
@AllArgsConstructor
public enum MenuCategoryEnum {

    MENU(1, "菜单"),
    BUTTON(2, "按钮");

    private Integer code;

    private String desc;

    public static String getMenuCategoryDesc(Integer code) {
        for (MenuCategoryEnum value : MenuCategoryEnum.values()) {
            if (value.getCode().intValue() == code.intValue()) {
                return value.getDesc();
            }
        }

        return null;
    }
}

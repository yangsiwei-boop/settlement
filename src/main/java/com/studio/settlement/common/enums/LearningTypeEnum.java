package com.studio.settlement.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 学习中心 - 学习方式
 */
@Getter
@AllArgsConstructor
public enum LearningTypeEnum {

    VIDEO_COURSE(1, "视频课程"),
    KNOWLEDGE_BASE(2, "知识库");

    private Integer code;

    private String desc;

}

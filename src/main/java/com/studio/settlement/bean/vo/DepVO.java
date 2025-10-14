package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
@Schema(description = "组织查询类")
public class DepVO {

    @Schema(description = "部门ID")
    private Long id;

    @Schema(description = "部门名称")
    private String depName;

    @Schema(description = "部门路径")
    private String depPath;

    @Schema(description = "节点编码")
    private String depCode;

    @Schema(description = "上级节点编码")
    private String parentCode;

    @Schema(description = "上级节点名")
    private String parentName;

    @Schema(description = "描述")
    private String comment;

    @Schema(description = "排序")
    private Integer sorts;

    @Schema(description = "是否公司级 1是 0不是")
    private Integer isComp;

    @Schema(description = "子部门")
    private List<DepVO> depVOS;

}

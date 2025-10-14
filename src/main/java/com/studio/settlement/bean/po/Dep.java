package com.studio.settlement.bean.po;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "机构表实体类")
@TableName("LF_DEP")
public class Dep {

    @TableId(value = "ID", type = IdType.AUTO)
    @Schema(description = "部门ID")
    private Long id;

    @TableField("DEP_NAME")
    @Schema(description = "部门名称")
    private String depName;

    @TableField("DEP_PATH")
    @Schema(description = "部门路径")
    private String depPath;

    @TableField("DEP_CODE")
    @Schema(description = "节点编码")
    private String depCode;

    @TableField("PARENT_CODE")
    @Schema(description = "上级节点编码")
    private String parentCode;

    @TableField("COMMENT")
    @Schema(description = "描述")
    private String comment;

    @TableField("SORTS")
    @Schema(description = "排序")
    private Integer sorts;

    @TableField("IS_COMP")
    @Schema(description = "是否公司级 1是 0不是")
    private Integer isComp;
}


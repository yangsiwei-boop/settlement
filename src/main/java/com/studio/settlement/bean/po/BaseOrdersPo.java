package com.studio.settlement.bean.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * <p>
 * 基础订单表（纯套餐模板）
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@Data
@Schema(description = "基础订单表（纯套餐模板）")
@TableName("yb_base_orders")
public class BaseOrdersPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "订单编号（BO年月日序列）")
    @TableField("order_code")
    private String orderCode;

    @Schema(description = "套餐名称（如：5+1套餐）")
    @TableField("package_name")
    private String packageName;

    @Schema(description = "购买数量（如：5）")
    @TableField("purchased_count")
    private Integer purchasedCount;

    @Schema(description = "赠送数量（如：1）")
    @TableField("gifted_count")
    private Integer giftedCount;

    @Schema(description = "总数量（purchased_count + gifted_count）")
    @TableField("total_count")
    private Integer totalCount;

    @Schema(description = "单小时标准单价")
    @TableField("unit_price")
    private BigDecimal unitPrice;

    @Schema(description = "订单总金额（purchased_count * unit_price）")
    @TableField("total_amount")
    private BigDecimal totalAmount;

    @Schema(description = "实付金额")
    @TableField("actual_amount")
    private BigDecimal actualAmount;

    @Schema(description = "实际单小时成本（actual_amount / total_count）")
    @TableField("unit_cost")
    private BigDecimal unitCost;

    @Schema(description = "打手结算比例")
    @TableField("settlement_rate")
    private BigDecimal settlementRate;

    @Schema(description = "有效天数")
    @TableField("valid_days")
    private Integer validDays;

    @Schema(description = "删除标识（0:正常 1:已删除）")
    @TableField("is_deleted")
    private Byte isDeleted;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private Date createTime;
}

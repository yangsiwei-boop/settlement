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
 * 实际执行单表（业务执行记录）
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@Data
@Schema(description = "实际执行单表（业务执行记录）")
@TableName("yb_actual_orders")
public class ActualOrdersPo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @Schema(description = "实际单编号（AO年月日序列）")
    @TableField("actual_order_code")
    private String actualOrderCode;

    @Schema(description = "关联的基础订单编号")
    @TableField("base_order_code")
    private String baseOrderCode;

    @Schema(description = "客户ID")
    @TableField("customer_id")
    private Long customerId;

    @Schema(description = "客户名称")
    @TableField("customer_name")
    private String customerName;

    @Schema(description = "销售员ID")
    @TableField("sales_id")
    private Long salesId;

    @Schema(description = "销售员姓名")
    @TableField("sales_name")
    private String salesName;

    @Schema(description = "打手ID")
    @TableField("server_id")
    private Long serverId;

    @Schema(description = "打手姓名")
    @TableField("server_name")
    private String serverName;

    @Schema(description = "服务类型")
    @TableField("service_type")
    private String serviceType;

    @Schema(description = "服务日期")
    @TableField("service_date")
    private Date serviceDate;

    @Schema(description = "服务小时数")
    @TableField("service_hours")
    private Integer serviceHours;

    @Schema(description = "基础单小时成本")
    @TableField("base_unit_cost")
    private BigDecimal baseUnitCost;

    @Schema(description = "基础结算比例")
    @TableField("base_settlement_rate")
    private BigDecimal baseSettlementRate;

    @Schema(description = "消费金额（base_unit_cost * service_hours）")
    @TableField("consumed_amount")
    private BigDecimal consumedAmount;

    @Schema(description = "打手结算金额（consumed_amount * base_settlement_rate）")
    @TableField("settlement_amount")
    private BigDecimal settlementAmount;

    @Schema(description = "订单状态（1:待服务 2:服务中 3:已完成 4:已取消）")
    @TableField("order_status")
    private Integer orderStatus;

    @Schema(description = "结算状态（0:未结算 1:已结算）")
    @TableField("settlement_status")
    private Integer settlementStatus;

    @Schema(description = "服务开始时间")
    @TableField("service_start_time")
    private Date serviceStartTime;

    @Schema(description = "服务结束时间")
    @TableField("service_end_time")
    private Date serviceEndTime;

    @Schema(description = "结算时间")
    @TableField("settlement_time")
    private Date settlementTime;

    @Schema(description = "创建时间")
    @TableField("create_time")
    private Date createTime;
}

package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 打手订单VO
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Data
@Schema(description = "打手订单VO")
public class ServerOrderVO {

    @Schema(description = "订单编号")
    private String orderCode;

    @Schema(description = "基础订单编号")
    private String baseOrderCode;

    @Schema(description = "客户姓名")
    private String customerName;

    @Schema(description = "服务类型")
    private String serviceType;

    @Schema(description = "服务日期")
    private Date serviceDate;

    @Schema(description = "服务时长(小时)")
    private Integer serviceHours;

    @Schema(description = "订单状态(1:待服务 2:服务中 3:已完成 4:已取消)")
    private Byte orderStatus;

    @Schema(description = "结算金额")
    private BigDecimal settlementAmount;

    @Schema(description = "服务开始时间")
    private Date serviceStartTime;

    @Schema(description = "服务结束时间")
    private Date serviceEndTime;

    @Schema(description = "创建时间")
    private Date createTime;
}

package com.studio.settlement.bean.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 打手信息VO
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Data
@Schema(description = "打手信息VO")
public class ServerVO {

    @Schema(description = "打手ID")
    private Long id;

    @Schema(description = "打手编号")
    private String serverCode;

    @Schema(description = "登录账号")
    private String username;

    @Schema(description = "真实姓名")
    private String realName;

    @Schema(description = "联系电话")
    private String phone;

    @Schema(description = "状态(1:正常 2:冻结)")
    private Byte serverStatus;

    @Schema(description = "在线状态(0:离线 1:在线)")
    private Byte onlineStatus;

    @Schema(description = "总接单数")
    private Integer totalOrders;

    @Schema(description = "完成订单数")
    private Integer completedOrders;

    @Schema(description = "总收入")
    private BigDecimal totalIncome;

    @Schema(description = "最后登录时间")
    private Date lastLoginTime;

    @Schema(description = "创建时间")
    private Date createTime;

    @Schema(description = "更新时间")
    private Date updateTime;
}

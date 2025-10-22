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
 * 打手基本信息表（简化版）
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Data
@Schema(description = "打手基本信息表（简化版）")
@TableName("yb_servers")
public class ServersPo implements Serializable {

    private static final long serialVersionUID = 1L;

            @Schema(description = "打手ID")
            @TableId(value = "id", type = IdType.AUTO)
            private Long id;

            @Schema(description = "打手编号（SR年月日序列）")
            @TableField("server_code")
            private String serverCode;

            @Schema(description = "登录账号")
            @TableField("username")
            private String username;

            @Schema(description = "加密密码")
            @TableField("password_hash")
            private String passwordHash;

            @Schema(description = "真实姓名")
            @TableField("real_name")
            private String realName;

            @Schema(description = "联系电话")
            @TableField("phone")
            private String phone;

            @Schema(description = "状态（1:正常 2:冻结）")
            @TableField("server_status")
            private Byte serverStatus;

            @Schema(description = "在线状态（0:离线 1:在线）")
            @TableField("online_status")
            private Byte onlineStatus;

            @Schema(description = "总接单数")
            @TableField("total_orders")
            private Integer totalOrders;

            @Schema(description = "完成订单数")
            @TableField("completed_orders")
            private Integer completedOrders;

            @Schema(description = "总收入")
            @TableField("total_income")
            private BigDecimal totalIncome;

            @Schema(description = "最后登录时间")
            @TableField("last_login_time")
            private Date lastLoginTime;

            @Schema(description = "创建时间")
            @TableField("create_time")
            private Date createTime;
}

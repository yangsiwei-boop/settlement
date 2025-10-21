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
 * 客户账户表）
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@Data
@Schema(description = "客户账户表）")
@TableName("yb_customer_accounts")
public class CustomerAccountsPo implements Serializable {

    private static final long serialVersionUID = 1L;

            @Schema(description = "账户ID")
            @TableId(value = "id", type = IdType.AUTO)
            private Long id;

            @Schema(description = "客户姓名（唯一标识）")
            @TableField("customer_name")
            private String customerName;

            @Schema(description = "历史总充值金额")
            @TableField("total_deposit")
            private BigDecimal totalDeposit;

            @Schema(description = "当前账户余额")
            @TableField("current_balance")
            private BigDecimal currentBalance;

            @Schema(description = "最后消费时间")
            @TableField("last_consumed_time")
            private Date lastConsumedTime;

            @Schema(description = "创建时间")
            @TableField("create_time")
            private Date createTime;

            @Schema(description = "更新时间")
            @TableField("update_time")
            private Date updateTime;
}

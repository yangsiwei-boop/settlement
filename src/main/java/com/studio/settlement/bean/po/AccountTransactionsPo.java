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
 * 账户变动记录表
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@Data
@Schema(description = "账户变动记录表")
@TableName("yb_account_transactions")
public class AccountTransactionsPo implements Serializable {

    private static final long serialVersionUID = 1L;

            @Schema(description = "记录ID")
            @TableId(value = "id", type = IdType.AUTO)
            private Long id;

            @Schema(description = "客户姓名")
            @TableField("customer_name")
            private String customerName;

            @Schema(description = "交易类型（1:充值 2:消费）")
            @TableField("transaction_type")
            private Byte transactionType;

            @Schema(description = "关联订单号")
            @TableField("related_order_code")
            private String relatedOrderCode;

            @Schema(description = "变动金额")
            @TableField("amount")
            private BigDecimal amount;

            @Schema(description = "变动后余额")
            @TableField("balance_after")
            private BigDecimal balanceAfter;

            @Schema(description = "操作人员")
            @TableField("operator")
            private String operator;

            @Schema(description = "备注")
            @TableField("notes")
            private String notes;

            @Schema(description = "创建时间")
            @TableField("create_time")
            private Date createTime;
}

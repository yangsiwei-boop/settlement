package com.studio.settlement.controller;

import com.studio.settlement.bean.po.ActualOrdersPo;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.service.OrderSettlementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * 订单结算控制器
 */
@RestController
@RequestMapping("/api/order-settlement")
@Tag(name = "订单结算管理", description = "订单结算相关接口")
public class OrderSettlementController {

    @Autowired
    private OrderSettlementService orderSettlementService;

    @Operation(summary = "完成订单并扣款", description = "完成订单并从客户账户扣除相应金额")
    @PostMapping("/complete-order")
    public ApiResult<Void> completeOrderAndDeductBalance(
            @Parameter(description = "订单ID") @RequestParam Long orderId,
            @Parameter(description = "客户姓名") @RequestParam String customerName,
            @Parameter(description = "消费金额") @RequestParam BigDecimal consumedAmount) {
        return orderSettlementService.completeOrderAndDeductBalance(orderId, customerName, consumedAmount);
    }

    @Operation(summary = "客户充值", description = "为客户账户充值")
    @PostMapping("/recharge")
    public ApiResult<Void> rechargeCustomerAccount(
            @Parameter(description = "客户姓名") @RequestParam String customerName,
            @Parameter(description = "充值金额") @RequestParam BigDecimal amount,
            @Parameter(description = "关联订单号") @RequestParam String orderCode,
            @Parameter(description = "操作人员") @RequestParam String operator) {
        return orderSettlementService.rechargeCustomerAccount(customerName, amount, orderCode, operator);
    }

    @Operation(summary = "获取订单详情", description = "根据ID获取订单详细信息")
    @GetMapping("/order-detail/{orderId}")
    public ApiResult<ActualOrdersPo> getOrderDetail(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return orderSettlementService.getOrderDetail(orderId);
    }

    @Operation(summary = "取消订单并退款", description = "取消订单并退还客户已支付的金额")
    @PostMapping("/cancel-order/{orderId}")
    public ApiResult<Void> cancelOrderAndRefund(
            @Parameter(description = "订单ID") @PathVariable Long orderId) {
        return orderSettlementService.cancelOrderAndRefund(orderId);
    }
}

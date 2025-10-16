package com.studio.settlement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.ActualOrdersPo;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.service.ActualOrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import java.util.List;

/**
 * <p>
 * 实际执行单表（业务执行记录） 前端控制器
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/api/actual-orders")
@Tag(name = "实际执行单管理", description = "实际执行单的增删改查接口")
public class ActualOrdersController {

    @Autowired
    private ActualOrdersService actualOrdersService;

    @Operation(summary = "创建实际执行单", description = "创建一个新的实际执行单")
    @PostMapping
    public ApiResult<ActualOrdersPo> create(@Valid @RequestBody ActualOrdersPo actualOrdersPo) {
        boolean success = actualOrdersService.createActualOrder(actualOrdersPo);
        if (success) {
            return ApiResult.ok("创建成功").data(actualOrdersPo);
        }
        return ApiResult.error("创建失败");
    }

    @Operation(summary = "更新实际执行单", description = "更新指定的实际执行单信息")
    @PostMapping("/{id}")
    public ApiResult<ActualOrdersPo> update(
            @Parameter(description = "执行单ID") @PathVariable Long id,
            @Valid @RequestBody ActualOrdersPo actualOrdersPo) {
        actualOrdersPo.setId(id);
        boolean success = actualOrdersService.updateActualOrder(actualOrdersPo);
        if (success) {
            return ApiResult.ok("更新成功").data(actualOrdersPo);
        }
        return ApiResult.error("更新失败");
    }

    @Operation(summary = "删除实际执行单", description = "删除指定的实际执行单")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@Parameter(description = "执行单ID") @PathVariable Long id) {
        boolean success = actualOrdersService.removeById(id);
        if (success) {
            return ApiResult.ok("删除成功");
        }
        return ApiResult.error("删除失败");
    }

    @Operation(summary = "批量删除实际执行单", description = "批量删除实际执行单")
    @DeleteMapping("/batch")
    public ApiResult<Void> batchDelete(@Parameter(description = "执行单ID列表") @RequestBody List<Long> ids) {
        boolean success = actualOrdersService.removeByIds(ids);
        if (success) {
            return ApiResult.ok("批量删除成功");
        }
        return ApiResult.error("批量删除失败");
    }

    @Operation(summary = "获取执行单详情", description = "根据ID获取实际执行单的详细信息")
    @GetMapping("/{id}")
    public ApiResult<ActualOrdersPo> getById(@Parameter(description = "执行单ID") @PathVariable Long id) {
        ActualOrdersPo order = actualOrdersService.getById(id);
        if (order != null) {
            return ApiResult.ok("查询成功").data(order);
        }
        return ApiResult.error("执行单不存在");
    }

    @Operation(summary = "分页查询实际执行单", description = "分页查询所有实际执行单")
    @GetMapping("/page")
    public ApiResult<IPage<ActualOrdersPo>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<ActualOrdersPo> page = new Page<>(current, size);
        IPage<ActualOrdersPo> result = actualOrdersService.pageActualOrders(page);
        return ApiResult.ok("查询成功").data(result);
    }

    @Operation(summary = "获取所有实际执行单", description = "获取所有实际执行单列表")
    @GetMapping("/list")
    public ApiResult<List<ActualOrdersPo>> list() {
        List<ActualOrdersPo> orders = actualOrdersService.list();
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "根据客户ID查询执行单", description = "根据客户ID查询相关的执行单")
    @GetMapping("/customer/{customerId}")
    public ApiResult<List<ActualOrdersPo>> getByCustomerId(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {
        List<ActualOrdersPo> orders = actualOrdersService.listByCustomerId(customerId);
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "根据打手ID查询执行单", description = "根据打手ID查询相关的执行单")
    @GetMapping("/server/{serverId}")
    public ApiResult<List<ActualOrdersPo>> getByServerId(
            @Parameter(description = "打手ID") @PathVariable Long serverId) {
        List<ActualOrdersPo> orders = actualOrdersService.listByServerId(serverId);
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "根据订单状态查询执行单", description = "根据订单状态查询执行单")
    @GetMapping("/status/{status}")
    public ApiResult<List<ActualOrdersPo>> getByStatus(
            @Parameter(description = "订单状态 (1:待服务 2:服务中 3:已完成 4:已取消)") @PathVariable Byte status) {
        List<ActualOrdersPo> orders = actualOrdersService.listByStatus(status);
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "根据结算状态查询执行单", description = "根据结算状态查询执行单")
    @GetMapping("/settlement-status/{status}")
    public ApiResult<List<ActualOrdersPo>> getBySettlementStatus(
            @Parameter(description = "结算状态 (0:未结算 1:已结算)") @PathVariable Byte status) {
        List<ActualOrdersPo> orders = actualOrdersService.listBySettlementStatus(status);
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "开始服务", description = "开始服务，记录服务开始时间")
    @PostMapping("/{id}/start-service")
    public ApiResult<Void> startService(@Parameter(description = "执行单ID") @PathVariable Long id) {
        boolean success = actualOrdersService.startService(id);
        if (success) {
            return ApiResult.ok("服务开始成功");
        }
        return ApiResult.error("服务开始失败");
    }

    @Operation(summary = "完成服务", description = "完成服务，记录服务结束时间")
    @PostMapping("/{id}/complete-service")
    public ApiResult<Void> completeService(@Parameter(description = "执行单ID") @PathVariable Long id) {
        boolean success = actualOrdersService.completeService(id);
        if (success) {
            return ApiResult.ok("服务完成成功");
        }
        return ApiResult.error("服务完成失败");
    }

    @Operation(summary = "结算执行单", description = "结算执行单，记录结算时间")
    @PostMapping("/{id}/settle")
    public ApiResult<Void> settleOrder(@Parameter(description = "执行单ID") @PathVariable Long id) {
        boolean success = actualOrdersService.settleOrder(id);
        if (success) {
            return ApiResult.ok("结算成功");
        }
        return ApiResult.error("结算失败");
    }

    @Operation(summary = "批量结算执行单", description = "批量结算执行单")
    @PostMapping("/batch-settle")
    public ApiResult<Void> batchSettleOrders(@Parameter(description = "执行单ID列表") @RequestBody List<Long> ids) {
        int successCount = actualOrdersService.batchSettleOrders(ids);
        return ApiResult.ok("成功结算 " + successCount + " 个执行单");
    }

    @Operation(summary = "取消执行单", description = "取消执行单")
    @PostMapping("/{id}/cancel")
    public ApiResult<Void> cancelOrder(@Parameter(description = "执行单ID") @PathVariable Long id) {
        boolean success = actualOrdersService.updateOrderStatus(id, (byte) 4); // 4表示已取消
        if (success) {
            return ApiResult.ok("取消成功");
        }
        return ApiResult.error("取消失败");
    }

    @Operation(summary = "根据基础订单查询执行单", description = "根据基础订单编号查询相关的执行单")
    @GetMapping("/base-order/{baseOrderCode}")
    public ApiResult<List<ActualOrdersPo>> getByBaseOrderCode(
            @Parameter(description = "基础订单编号") @PathVariable String baseOrderCode) {
        List<ActualOrdersPo> orders = actualOrdersService.list(
                new LambdaQueryWrapper<ActualOrdersPo>()
                        .eq(ActualOrdersPo::getBaseOrderCode, baseOrderCode)
                        .orderByDesc(ActualOrdersPo::getCreateTime)
        );
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "获取打手业绩统计", description = "获取指定打手的业绩统计信息")
    @GetMapping("/server/{serverId}/stats")
    public ApiResult<Map<String, Object>> getServerStats(
            @Parameter(description = "打手ID") @PathVariable Long serverId,
            @Parameter(description = "开始日期 (yyyy-MM-dd)") @RequestParam(required = false) String startDate,
            @Parameter(description = "结束日期 (yyyy-MM-dd)") @RequestParam(required = false) String endDate) {

        List<ActualOrdersPo> orders = actualOrdersService.listByServerId(serverId);

        // 过滤日期范围
        if (startDate != null && endDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                Date start = sdf.parse(startDate);
                Date end = sdf.parse(endDate);
                orders = orders.stream()
                        .filter(order -> order.getServiceDate() != null &&
                                order.getServiceDate().after(start) &&
                                order.getServiceDate().before(end))
                        .collect(Collectors.toList());
            } catch (ParseException e) {
                return ApiResult.error("日期格式不正确，请使用yyyy-MM-dd格式");
            }
        }

        // 计算统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orders.size());
        stats.put("totalHours", orders.stream().mapToInt(ActualOrdersPo::getServiceHours).sum());
        stats.put("totalConsumedAmount", orders.stream()
                .map(ActualOrdersPo::getConsumedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("totalSettlementAmount", orders.stream()
                .map(ActualOrdersPo::getSettlementAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("unsettledAmount", orders.stream()
                .filter(order -> order.getSettlementStatus() != null && order.getSettlementStatus() == 0)
                .map(ActualOrdersPo::getSettlementAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));

        return ApiResult.ok("查询成功").data(stats);
    }

    @Operation(summary = "获取客户消费统计", description = "获取指定客户的消费统计信息")
    @GetMapping("/customer/{customerId}/stats")
    public ApiResult<Map<String, Object>> getCustomerStats(
            @Parameter(description = "客户ID") @PathVariable Long customerId) {

        List<ActualOrdersPo> orders = actualOrdersService.listByCustomerId(customerId);

        // 计算统计信息
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalOrders", orders.size());
        stats.put("totalHours", orders.stream().mapToInt(ActualOrdersPo::getServiceHours).sum());
        stats.put("totalConsumedAmount", orders.stream()
                .map(ActualOrdersPo::getConsumedAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
        stats.put("favoriteService", orders.stream()
                .collect(Collectors.groupingBy(ActualOrdersPo::getServiceType, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("无"));

        return ApiResult.ok("查询成功").data(stats);
    }
}

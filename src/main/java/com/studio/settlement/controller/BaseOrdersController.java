package com.studio.settlement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.BaseOrdersPo;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.service.BaseOrdersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 基础订单表（纯套餐模板） 前端控制器
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@RestController
@RequestMapping("/base-orders")
@Tag(name = "基础订单管理", description = "基础订单的增删改查接口")
public class BaseOrdersController {

    @Autowired
    private BaseOrdersService baseOrdersService;

    @Operation(summary = "创建基础订单", description = "创建一个新的基础订单模板")
    @PostMapping
    public ApiResult<BaseOrdersPo> create(@Valid @RequestBody BaseOrdersPo baseOrdersPo) {
        boolean success = baseOrdersService.createOrder(baseOrdersPo);
        if (success) {
            return ApiResult.ok("创建成功").data(baseOrdersPo);
        }
        return ApiResult.error("创建失败");
    }

    @Operation(summary = "更新基础订单", description = "更新指定的基础订单信息")
    @PostMapping("/{id}")
    public ApiResult<BaseOrdersPo> update(
            @Parameter(description = "订单ID") @PathVariable Long id,
            @Valid @RequestBody BaseOrdersPo baseOrdersPo) {
        baseOrdersPo.setId(id);
        boolean success = baseOrdersService.updateOrder(baseOrdersPo);
        if (success) {
            return ApiResult.ok("更新成功").data(baseOrdersPo);
        }
        return ApiResult.error("更新失败");
    }

    @Operation(summary = "删除基础订单", description = "逻辑删除指定的基础订单")
    @DeleteMapping("/{id}")
    public ApiResult<Void> delete(@Parameter(description = "订单ID") @PathVariable Long id) {
        boolean success = baseOrdersService.logicDeleteById(id);
        if (success) {
            return ApiResult.ok("删除成功");
        }
        return ApiResult.error("删除失败");
    }

    @Operation(summary = "批量删除基础订单", description = "批量逻辑删除基础订单")
    @DeleteMapping("/batch")
    public ApiResult<Void> batchDelete(@Parameter(description = "订单ID列表") @RequestBody List<Long> ids) {
        boolean success = baseOrdersService.logicDeleteBatchIds(ids);
        if (success) {
            return ApiResult.ok("批量删除成功");
        }
        return ApiResult.error("批量删除失败");
    }

    @Operation(summary = "获取订单详情", description = "根据ID获取基础订单的详细信息")
    @GetMapping("/{id}")
    public ApiResult<BaseOrdersPo> getById(@Parameter(description = "订单ID") @PathVariable Long id) {
        BaseOrdersPo order = baseOrdersService.getByIdNotDeleted(id);
        if (order != null) {
            return ApiResult.ok("查询成功").data(order);
        }
        return ApiResult.error("订单不存在或已被删除");
    }

    @Operation(summary = "分页查询基础订单", description = "分页查询所有基础订单")
    @GetMapping("/page")
    public ApiResult<IPage<BaseOrdersPo>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<BaseOrdersPo> page = new Page<>(current, size);
        IPage<BaseOrdersPo> result = baseOrdersService.pageNotDeleted(page);
        return ApiResult.ok("查询成功").data(result);
    }

    @Operation(summary = "获取所有基础订单", description = "获取所有未删除的基础订单列表")
    @GetMapping("/list")
    public ApiResult<List<BaseOrdersPo>> list() {
        List<BaseOrdersPo> orders = baseOrdersService.listNotDeleted();
        return ApiResult.ok("查询成功").data(orders);
    }

    @Operation(summary = "根据套餐名称查询", description = "根据套餐名称模糊查询基础订单")
    @GetMapping("/search")
    public ApiResult<List<BaseOrdersPo>> searchByName(
            @Parameter(description = "套餐名称") @RequestParam String packageName) {
        List<BaseOrdersPo> orders = baseOrdersService.list(new LambdaQueryWrapper<BaseOrdersPo>()
                .like(BaseOrdersPo::getPackageName, packageName)
                .eq(BaseOrdersPo::getIsDeleted, 0));
        return ApiResult.ok("查询成功").data(orders);
    }


    @Operation(summary = "复制套餐", description = "复制现有套餐创建新套餐")
    @PostMapping("/copy/{id}")
    public ApiResult<BaseOrdersPo> copyPackage(
            @Parameter(description = "源套餐ID") @PathVariable Long id,
            @Parameter(description = "新套餐名称") @RequestParam String newPackageName) {
        BaseOrdersPo source = baseOrdersService.getByIdNotDeleted(id);
        if (source == null) {
            return ApiResult.error("源套餐不存在");
        }

        // 复制套餐
        BaseOrdersPo newPackage = new BaseOrdersPo();
        newPackage.setPackageName(newPackageName);
        newPackage.setPurchasedCount(source.getPurchasedCount());
        newPackage.setGiftedCount(source.getGiftedCount());
        newPackage.setUnitPrice(source.getUnitPrice());
        newPackage.setActualAmount(source.getActualAmount());
        newPackage.setSettlementRate(source.getSettlementRate());
        newPackage.setValidDays(source.getValidDays());

        boolean success = baseOrdersService.createOrder(newPackage);
        if (success) {
            return ApiResult.ok("复制成功").data(newPackage);
        }
        return ApiResult.error("复制失败");
    }

    @Operation(summary = "批量更新结算比例", description = "批量更新基础订单的结算比例")
    @PutMapping("/batch-settlement-rate")
    public ApiResult<Void> batchUpdateSettlementRate(
            @Parameter(description = "订单ID列表") @RequestBody List<Long> ids,
            @Parameter(description = "新结算比例") @RequestParam BigDecimal newRate) {
        if (newRate.compareTo(BigDecimal.ZERO) < 0 || newRate.compareTo(BigDecimal.ONE) > 0) {
            return ApiResult.error("结算比例必须在0到1之间");
        }

        int updated = 0;
        for (Long id : ids) {
            BaseOrdersPo order = baseOrdersService.getByIdNotDeleted(id);
            if (order != null) {
                order.setSettlementRate(newRate);
                if (baseOrdersService.updateOrder(order)) {
                    updated++;
                }
            }
        }

        return ApiResult.ok("成功更新 " + updated + " 个订单的结算比例");
    }

    @Operation(summary = "恢复已删除的订单", description = "恢复被逻辑删除的基础订单")
    @PostMapping("/restore/{id}")
    public ApiResult<Void> restoreOrder(@Parameter(description = "订单ID") @PathVariable Long id) {
        BaseOrdersPo entity = new BaseOrdersPo();
        entity.setId(id);
        entity.setIsDeleted((byte) 0);
        boolean success = baseOrdersService.updateById(entity);
        if (success) {
            return ApiResult.ok("恢复成功");
        }
        return ApiResult.error("恢复失败");
    }

    @Operation(summary = "获取套餐类型统计", description = "统计各类套餐的数量和使用情况")
    @GetMapping("/package-stats")
    public ApiResult<Map<String, Object>> getPackageStatistics() {
        List<BaseOrdersPo> orders = baseOrdersService.listNotDeleted();

        Map<String, Long> packageCount = orders.stream()
                .collect(Collectors.groupingBy(BaseOrdersPo::getPackageName, Collectors.counting()));

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalPackages", orders.size());
        stats.put("packageDistribution", packageCount);
        stats.put("totalPurchased", orders.stream().mapToInt(BaseOrdersPo::getPurchasedCount).sum());
        stats.put("totalGifted", orders.stream().mapToInt(BaseOrdersPo::getGiftedCount).sum());

        return ApiResult.ok("统计成功").data(stats);
    }
}

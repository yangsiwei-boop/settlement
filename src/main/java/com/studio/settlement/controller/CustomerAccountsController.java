package com.studio.settlement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.CustomerAccountsPo;
import com.studio.settlement.bean.vo.CustomerAccountsVO;
import com.studio.settlement.bean.vo.AccountStatisticsVO;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.service.CustomerAccountsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户账户表 前端控制器
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@RestController
@RequestMapping("/settlement/customer-accounts")
@Tag(name = "客户账户管理", description = "客户账户的查询和管理接口")
public class CustomerAccountsController {

    @Autowired
    private CustomerAccountsService customerAccountsService;

    @Operation(summary = "获取客户账户详情", description = "根据ID获取客户账户的详细信息")
    @GetMapping("/{id}")
    public ApiResult<CustomerAccountsVO> getById(
            @Parameter(description = "账户ID") @PathVariable Long id) {
        CustomerAccountsPo account = customerAccountsService.getById(id);
        if (account != null) {
            return ApiResult.ok("查询成功").data(convertToVO(account));
        }
        return ApiResult.error("账户不存在");
    }

    @Operation(summary = "根据客户姓名获取账户", description = "根据客户姓名获取客户账户信息")
    @GetMapping("/by-name/{customerName}")
    public ApiResult<CustomerAccountsVO> getByCustomerName(
            @Parameter(description = "客户姓名") @PathVariable String customerName) {
        CustomerAccountsPo account = customerAccountsService.getByCustomerName(customerName);
        if (account != null) {
            return ApiResult.ok("查询成功").data(convertToVO(account));
        }
        return ApiResult.error("客户账户不存在");
    }

    @Operation(summary = "分页查询客户账户", description = "分页查询所有客户账户")
    @GetMapping("/page")
    public ApiResult<IPage<CustomerAccountsVO>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<CustomerAccountsPo> page = new Page<>(current, size);
        IPage<CustomerAccountsPo> result = customerAccountsService.page(page);

        // 转换为VO分页
        IPage<CustomerAccountsVO> voPage = result.convert(this::convertToVO);
        return ApiResult.ok("查询成功").data(voPage);
    }

    @Operation(summary = "获取所有客户账户", description = "获取所有客户账户列表")
    @GetMapping("/list")
    public ApiResult<List<CustomerAccountsVO>> list() {
        List<CustomerAccountsPo> accounts = customerAccountsService.list();

        List<CustomerAccountsVO> voList = accounts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "查询余额充足的客户", description = "查询余额大于指定金额的客户账户")
    @GetMapping("/with-balance-above")
    public ApiResult<List<CustomerAccountsVO>> getWithBalanceAbove(
            @Parameter(description = "最低余额") @RequestParam BigDecimal minBalance) {
        List<CustomerAccountsPo> accounts = customerAccountsService.list(
                new LambdaQueryWrapper<CustomerAccountsPo>()
                        .ge(CustomerAccountsPo::getCurrentBalance, minBalance)
                        .orderByDesc(CustomerAccountsPo::getCurrentBalance)
        );

        List<CustomerAccountsVO> voList = accounts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "查询余额不足的客户", description = "查询余额小于指定金额的客户账户")
    @GetMapping("/with-balance-below")
    public ApiResult<List<CustomerAccountsVO>> getWithBalanceBelow(
            @Parameter(description = "最高余额") @RequestParam BigDecimal maxBalance) {
        List<CustomerAccountsPo> accounts = customerAccountsService.list(
                new LambdaQueryWrapper<CustomerAccountsPo>()
                        .le(CustomerAccountsPo::getCurrentBalance, maxBalance)
                        .orderByAsc(CustomerAccountsPo::getCurrentBalance)
        );

        List<CustomerAccountsVO> voList = accounts.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }


    @Operation(summary = "获取账户统计信息", description = "获取客户账户的统计信息")
    @GetMapping("/statistics")
    public ApiResult<AccountStatisticsVO> getAccountStatistics() {
        List<CustomerAccountsPo> accounts = customerAccountsService.list();

        AccountStatisticsVO statistics = new AccountStatisticsVO();
        statistics.setTotalAccounts(accounts.size());

        BigDecimal totalBalance = BigDecimal.ZERO;
        int activeAccounts = 0;
        int frozenAccounts = 0;
        int closedAccounts = 0;

        for (CustomerAccountsPo account : accounts) {
            totalBalance = totalBalance.add(account.getCurrentBalance());

            activeAccounts++;
        }

        statistics.setTotalBalance(totalBalance);
        statistics.setAverageBalance(accounts.isEmpty() ? BigDecimal.ZERO :
                totalBalance.divide(BigDecimal.valueOf(accounts.size()), 2, RoundingMode.HALF_UP));
        statistics.setActiveAccounts(activeAccounts);
        statistics.setFrozenAccounts(frozenAccounts);
        statistics.setClosedAccounts(closedAccounts);

        return ApiResult.ok("查询成功").data(statistics);
    }

    /**
     * 将PO转换为VO
     */
    private CustomerAccountsVO convertToVO(CustomerAccountsPo po) {
        if (po == null) {
            return null;
        }

        CustomerAccountsVO vo = new CustomerAccountsVO();
        vo.setId(po.getId());
        vo.setCustomerName(po.getCustomerName());
        vo.setTotalDeposit(po.getTotalDeposit());
        vo.setCurrentBalance(po.getCurrentBalance());
        vo.setLastConsumedTime(po.getLastConsumedTime());
        vo.setCreateTime(po.getCreateTime());
        vo.setUpdateTime(po.getUpdateTime());

        return vo;
    }
}

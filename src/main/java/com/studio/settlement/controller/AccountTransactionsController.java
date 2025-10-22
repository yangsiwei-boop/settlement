package com.studio.settlement.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.AccountTransactionsPo;
import com.studio.settlement.bean.vo.AccountTransactionsVO;
import com.studio.settlement.bean.vo.TransactionSummaryVO;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.service.AccountTransactionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 账户变动记录表 前端控制器
 * </p>
 *
 * @author YourName
 * @since 2025-10-21
 */
@RestController
@RequestMapping("/account-transactions")
@Tag(name = "账户变动记录管理", description = "账户变动记录的查询接口")
public class AccountTransactionsController {

    @Autowired
    private AccountTransactionsService accountTransactionsService;

    @Operation(summary = "获取账户变动记录详情", description = "根据ID获取账户变动记录的详细信息")
    @GetMapping("/{id}")
    public ApiResult<AccountTransactionsVO> getById(
            @Parameter(description = "记录ID") @PathVariable Long id) {
        AccountTransactionsPo record = accountTransactionsService.getById(id);
        if (record != null) {
            return ApiResult.ok("查询成功").data(convertToVO(record));
        }
        return ApiResult.error("记录不存在");
    }

    @Operation(summary = "分页查询账户变动记录", description = "分页查询所有账户变动记录")
    @GetMapping("/page")
    public ApiResult<IPage<AccountTransactionsVO>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "极速版每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<AccountTransactionsPo> page = new Page<>(current, size);
        IPage<AccountTransactionsPo> result = accountTransactionsService.page(page);

        // 转换为VO分页
        IPage<AccountTransactionsVO> voPage = result.convert(this::convertToVO);
        return ApiResult.ok("查询成功").data(voPage);
    }

    @Operation(summary = "根据客户姓名查询变动记录", description = "根据客户姓名查询相关的账户变动记录")
    @GetMapping("/customer/{customerName}")
    public ApiResult<List<AccountTransactionsVO>> getByCustomerName(
            @Parameter(description = "客户姓名") @PathVariable String customerName) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .eq(AccountTransactionsPo::getCustomerName, customerName)
                        .orderByDesc(AccountTransactionsPo::getCreateTime)
        );

        List<AccountTransactionsVO> voList = records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "根据订单号查询变动记录", description = "根据关联订单号查询账户变动记录")
    @GetMapping("/order/{orderCode}")
    public ApiResult<List<AccountTransactionsVO>> getByOrderCode(
            @Parameter(description = "订单号") @PathVariable String orderCode) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .eq(AccountTransactionsPo::getRelatedOrderCode, orderCode)
                        .orderByDesc(AccountTransactionsPo::getCreateTime)
        );

        List<AccountTransactionsVO> voList = records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "根据交易类型查询变动记录", description = "根据交易类型查询账户变动记录")
    @GetMapping("/type/{transactionType}")
    public ApiResult<List<AccountTransactionsVO>> getByTransactionType(
            @Parameter(description = "交易类型 (1:充值 2:消费)") @PathVariable Integer transactionType) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .eq(AccountTransactionsPo::getTransactionType, transactionType)
                        .orderByDesc(AccountTransactionsPo::getCreateTime)
        );

        List<AccountTransactionsVO> voList = records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "获取客户最近交易记录", description = "获取客户最近的账户变动记录")
    @GetMapping("/customer/{customerName}/recent")
    public ApiResult<List<AccountTransactionsVO>> getRecentTransactions(
            @Parameter(description = "客户姓名") @PathVariable String customerName,
            @Parameter(description = "记录数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .eq(AccountTransactionsPo::getCustomerName, customerName)
                        .orderByDesc(AccountTransactionsPo::getCreateTime)
                        .last("LIMIT " + limit)
        );

        List<AccountTransactionsVO> voList = records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "按时间范围查询变动记录", description = "按时间范围查询账户变动记录")
    @GetMapping("/time-range")
    public ApiResult<List<AccountTransactionsVO>> getByTimeRange(
            @Parameter(description = "开始时间 (格式: yyyy-MM-dd HH:mm:ss)") @RequestParam String startTime,
            @Parameter(description = "结束时间 (格式: yyyy-MM-dd HH:mm:ss)") @RequestParam String endTime) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .between(AccountTransactionsPo::getCreateTime, startTime, endTime)
                        .orderByDesc(AccountTransactionsPo::getCreateTime)
        );

        List<AccountTransactionsVO> voList = records.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        return ApiResult.ok("查询成功").data(voList);
    }

    @Operation(summary = "统计客户交易总额", description = "统计客户的总充值金额和总消费金额")
    @GetMapping("/customer/{customerName}/summary")
    public ApiResult<TransactionSummaryVO> getTransactionSummary(
            @Parameter(description = "客户姓名") @PathVariable String customerName) {
        List<AccountTransactionsPo> records = accountTransactionsService.list(
                new LambdaQueryWrapper<AccountTransactionsPo>()
                        .eq(AccountTransactionsPo::getCustomerName, customerName)
        );

        TransactionSummaryVO summary = new TransactionSummaryVO();
        summary.setCustomerName(customerName);

        for (AccountTransactionsPo record : records) {
            if (record.getTransactionType() == 1) { // 充值
                summary.setTotalDeposit(summary.getTotalDeposit().add(record.getAmount()));
            } else if (record.getTransactionType() == 2) { // 消费
                summary.setTotalConsumption(summary.getTotalConsumption().add(record.getAmount().abs()));
            }
        }

        summary.setNetAmount(summary.getTotalDeposit().subtract(summary.getTotalConsumption()));

        return ApiResult.ok("查询成功").data(summary);
    }

    /**
     * 将PO转换为VO
     */
    private AccountTransactionsVO convertToVO(AccountTransactionsPo po) {
        if (po == null) {
            return null;
        }

        AccountTransactionsVO vo = new AccountTransactionsVO();
        vo.setId(po.getId());
        vo.setCustomerName(po.getCustomerName());
        vo.setTransactionType(po.getTransactionType());
        vo.setRelatedOrderCode(po.getRelatedOrderCode());
        vo.setAmount(po.getAmount());
        vo.setBalanceAfter(po.getBalanceAfter());
        vo.setOperator(po.getOperator());
        vo.setNotes(po.getNotes());
        vo.setCreateTime(po.getCreateTime());

        return vo;
    }
}

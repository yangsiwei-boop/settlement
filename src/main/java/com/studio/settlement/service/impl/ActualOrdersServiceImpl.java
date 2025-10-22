package com.studio.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studio.settlement.bean.po.ActualOrdersPo;
import com.studio.settlement.mapper.ActualOrdersMapper;
import com.studio.settlement.service.ActualOrdersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 实际执行单表（业务执行记录） 服务实现类
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@Service
public class ActualOrdersServiceImpl extends ServiceImpl<ActualOrdersMapper, ActualOrdersPo> implements ActualOrdersService {

    @Override
    public boolean save(ActualOrdersPo entity) {
        // 保存前自动计算金额
        calculateAmounts(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(ActualOrdersPo entity) {
        // 更新前自动计算金额
        calculateAmounts(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean createActualOrder(ActualOrdersPo entity) {
        // 生成实际单编号（如果未提供）
        if (entity.getActualOrderCode() == null) {
            entity.setActualOrderCode(generateActualOrderCode());
        }

        // 设置默认状态
        if (entity.getOrderStatus() == null) {
            entity.setOrderStatus(1); // 默认待服务状态
        }

        // 设置创建时间
        if (entity.getCreateTime() == null) {
            entity.setCreateTime(new Date());
        }

        return save(entity);
    }

    @Override
    public boolean updateActualOrder(ActualOrdersPo entity) {
        Assert.notNull(entity.getId(), "执行单ID不能为空");
        return updateById(entity);
    }

    @Override
    public boolean updateOrderStatus(Long id, int status) {
        ActualOrdersPo entity = new ActualOrdersPo();
        entity.setId(id);
        entity.setOrderStatus(status);
        return updateById(entity);
    }

    @Override
    public boolean startService(Long id) {
        ActualOrdersPo entity = new ActualOrdersPo();
        entity.setId(id);
        entity.setOrderStatus(2); // 服务中状态
        entity.setServiceStartTime(new Date());
        return updateById(entity);
    }

    @Override
    public boolean completeService(Long id) {
        ActualOrdersPo entity = new ActualOrdersPo();
        entity.setId(id);
        entity.setOrderStatus(3); // 已完成状态
        entity.setServiceEndTime(new Date());
        return updateById(entity);
    }

    @Override
    public boolean settleOrder(Long id) {
        ActualOrdersPo entity = new ActualOrdersPo();
        entity.setId(id);
        entity.setSettlementStatus(1); // 已结算状态
        entity.setSettlementTime(new Date());
        return updateById(entity);
    }

    @Override
    public int batchSettleOrders(List<Long> ids) {
        int successCount = 0;
        for (Long id : ids) {
            if (settleOrder(id)) {
                successCount++;
            }
        }
        return successCount;
    }

    @Override
    public List<ActualOrdersPo> listByCustomerId(Long customerId) {
        return list(new LambdaQueryWrapper<ActualOrdersPo>()
                .eq(ActualOrdersPo::getCustomerId, customerId)
                .orderByDesc(ActualOrdersPo::getCreateTime));
    }

    @Override
    public List<ActualOrdersPo> listByServerId(Long serverId) {
        return list(new LambdaQueryWrapper<ActualOrdersPo>()
                .eq(ActualOrdersPo::getServerId, serverId)
                .orderByDesc(ActualOrdersPo::getCreateTime));
    }

    @Override
    public IPage<ActualOrdersPo> pageActualOrders(Page<ActualOrdersPo> page) {
        return page(page, new LambdaQueryWrapper<ActualOrdersPo>()
                .orderByDesc(ActualOrdersPo::getCreateTime));
    }

    @Override
    public List<ActualOrdersPo> listByStatus(int status) {
        return list(new LambdaQueryWrapper<ActualOrdersPo>()
                .eq(ActualOrdersPo::getOrderStatus, status)
                .orderByDesc(ActualOrdersPo::getCreateTime));
    }

    @Override
    public List<ActualOrdersPo> listBySettlementStatus(int settlementStatus) {
        return list(new LambdaQueryWrapper<ActualOrdersPo>()
                .eq(ActualOrdersPo::getSettlementStatus, settlementStatus)
                .orderByDesc(ActualOrdersPo::getCreateTime));
    }

    /**
     * 计算消费金额和结算金额
     * @param entity 执行单实体
     */
    private void calculateAmounts(ActualOrdersPo entity) {
        // 计算消费金额：基础单小时成本 * 服务小时数
        if (entity.getBaseUnitCost() != null && entity.getServiceHours() != null) {
            BigDecimal serviceHours = BigDecimal.valueOf(entity.getServiceHours());
            entity.setConsumedAmount(entity.getBaseUnitCost().multiply(serviceHours));
        }

        // 计算结算金额：消费金额 * 结算比例
        if (entity.getConsumedAmount() != null && entity.getBaseSettlementRate() != null) {
            entity.setSettlementAmount(
                    entity.getConsumedAmount().multiply(entity.getBaseSettlementRate())
                            .setScale(2, RoundingMode.HALF_UP) // 保留两位小数
            );
        }
    }

    /**
     * 生成实际单编号（格式：AO年月日+序列）
     * @return 实际单编号
     */
    private String generateActualOrderCode() {
        // 这里简单实现，实际项目中可以根据需要完善
        return "AO" + System.currentTimeMillis();
    }
}

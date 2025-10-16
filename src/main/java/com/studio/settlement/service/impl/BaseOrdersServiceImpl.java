package com.studio.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.studio.settlement.bean.po.BaseOrdersPo;
import com.studio.settlement.mapper.BaseOrdersMapper;
import com.studio.settlement.service.BaseOrdersService;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * <p>
 * 基础订单表（纯套餐模板） 服务实现类
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
@Service
public class BaseOrdersServiceImpl extends ServiceImpl<BaseOrdersMapper, BaseOrdersPo> implements BaseOrdersService {

    @Override
    public boolean save(BaseOrdersPo entity) {
        // 保存前自动计算衍生字段
        calculateDerivedFields(entity);
        return super.save(entity);
    }

    @Override
    public boolean updateById(BaseOrdersPo entity) {
        // 更新前自动计算衍生字段
        calculateDerivedFields(entity);
        return super.updateById(entity);
    }

    @Override
    public boolean logicDeleteById(Long id) {
        BaseOrdersPo entity = new BaseOrdersPo();
        entity.setId(id);
        entity.setIsDeleted((byte) 1);
        return updateById(entity);
    }

    @Override
    public boolean logicDeleteBatchIds(List<Long> ids) {
        return ids.stream().allMatch(this::logicDeleteById);
    }

    @Override
    public BaseOrdersPo getByIdNotDeleted(Long id) {
        return getOne(new LambdaQueryWrapper<BaseOrdersPo>()
                .eq(BaseOrdersPo::getId, id)
                .eq(BaseOrdersPo::getIsDeleted, 0));
    }

    @Override
    public List<BaseOrdersPo> listNotDeleted() {
        return list(new LambdaQueryWrapper<BaseOrdersPo>()
                .eq(BaseOrdersPo::getIsDeleted, 0));
    }

    @Override
    public IPage<BaseOrdersPo> pageNotDeleted(Page<BaseOrdersPo> page) {
        return page(page, new LambdaQueryWrapper<BaseOrdersPo>()
                .eq(BaseOrdersPo::getIsDeleted, 0));
    }

    @Override
    public boolean createOrder(BaseOrdersPo entity) {
        // 生成订单编号（如果未提供）
        if (entity.getOrderCode() == null) {
            entity.setOrderCode(generateOrderCode());
        }
        return save(entity);
    }

    @Override
    public boolean updateOrder(BaseOrdersPo entity) {
        Assert.notNull(entity.getId(), "订单ID不能为空");
        return updateById(entity);
    }

    /**
     * 计算订单的衍生字段
     * @param entity 订单实体
     */
    private void calculateDerivedFields(BaseOrdersPo entity) {
        // 计算总数量：购买数量 + 赠送数量
        if (entity.getPurchasedCount() != null && entity.getGiftedCount() != null) {
            entity.setTotalCount(entity.getPurchasedCount() + entity.getGiftedCount());
        }

        // 计算订单总金额：购买数量 * 单价
        if (entity.getPurchasedCount() != null && entity.getUnitPrice() != null) {
            BigDecimal purchasedCount = BigDecimal.valueOf(entity.getPurchasedCount());
            entity.setTotalAmount(entity.getUnitPrice().multiply(purchasedCount));
        }

        // 计算实际单小时成本：实付金额 / 总数量
        if (entity.getActualAmount() != null && entity.getTotalCount() != null && entity.getTotalCount() > 0) {
            BigDecimal totalCount = BigDecimal.valueOf(entity.getTotalCount());
            entity.setUnitCost(entity.getActualAmount().divide(totalCount, 2, RoundingMode.HALF_UP));
        }
    }

    /**
     * 生成订单编号（格式：BO年月日+序列）
     * @return 订单编号
     */
    private String generateOrderCode() {
        // 这里简单实现，实际项目中可以根据需要完善
        return "BO" + System.currentTimeMillis();
    }
}

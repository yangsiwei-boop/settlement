package com.studio.settlement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.po.BaseOrdersPo;

import java.util.List;

/**
 * <p>
 * 基础订单表（纯套餐模板） 服务类
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
public interface BaseOrdersService extends IService<BaseOrdersPo> {

    /**
     * 逻辑删除订单（软删除）
     * @param id 订单ID
     * @return 是否删除成功
     */
    boolean logicDeleteById(Long id);

    /**
     * 批量逻辑删除订单
     * @param ids 订单ID列表
     * @return 是否删除成功
     */
    boolean logicDeleteBatchIds(List<Long> ids);

    /**
     * 根据ID获取未删除的订单
     * @param id 订单ID
     * @return 订单信息
     */
    BaseOrdersPo getByIdNotDeleted(Long id);

    /**
     * 获取所有未删除的订单列表
     * @return 订单列表
     */
    List<BaseOrdersPo> listNotDeleted();

    /**
     * 分页查询未删除的订单
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<BaseOrdersPo> pageNotDeleted(Page<BaseOrdersPo> page);

    /**
     * 创建订单并自动计算衍生字段
     * @param entity 订单实体
     * @return 是否创建成功
     */
    boolean createOrder(BaseOrdersPo entity);

    /**
     * 更新订单并自动计算衍生字段
     * @param entity 订单实体
     * @return 是否更新成功
     */
    boolean updateOrder(BaseOrdersPo entity);
}

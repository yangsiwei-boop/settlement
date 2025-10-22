package com.studio.settlement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.po.ActualOrdersPo;

import java.util.List;

/**
 * <p>
 * 实际执行单表（业务执行记录） 服务类
 * </p>
 *
 * @author YourName
 * @since 2025-10-16
 */
public interface ActualOrdersService extends IService<ActualOrdersPo> {

    /**
     * 创建实际执行单并自动计算金额
     * @param entity 执行单实体
     * @return 是否创建成功
     */
    boolean createActualOrder(ActualOrdersPo entity);

    /**
     * 更新实际执行单并重新计算金额
     * @param entity 执行单实体
     * @return 是否更新成功
     */
    boolean updateActualOrder(ActualOrdersPo entity);

    /**
     * 更新订单状态
     * @param id 执行单ID
     * @param status 新状态（1:待服务 2:服务中 3:已完成 4:已取消）
     * @return 是否更新成功
     */
    boolean updateOrderStatus(Long id, int status);

    /**
     * 开始服务（记录开始时间）
     * @param id 执行单ID
     * @return 是否操作成功
     */
    boolean startService(Long id);

    /**
     * 完成服务（记录结束时间并更新状态）
     * @param id 执行单ID
     * @return 是否操作成功
     */
    boolean completeService(Long id);

    /**
     * 结算执行单（更新结算状态和结算时间）
     * @param id 执行单ID
     * @return 是否结算成功
     */
    boolean settleOrder(Long id);

    /**
     * 批量结算执行单
     * @param ids 执行单ID列表
     * @return 成功结算的数量
     */
    int batchSettleOrders(List<Long> ids);

    /**
     * 根据客户ID查询执行单
     * @param customerId 客户ID
     * @return 执行单列表
     */
    List<ActualOrdersPo> listByCustomerId(Long customerId);

    /**
     * 根据打手ID查询执行单
     * @param serverId 打手ID
     * @return 执行单列表
     */
    List<ActualOrdersPo> listByServerId(Long serverId);

    /**
     * 分页查询执行单
     * @param page 分页参数
     * @return 分页结果
     */
    IPage<ActualOrdersPo> pageActualOrders(Page<ActualOrdersPo> page);

    /**
     * 根据状态查询执行单
     * @param status 订单状态
     * @return 执行单列表
     */
    List<ActualOrdersPo> listByStatus(int status);

    /**
     * 根据结算状态查询执行单
     * @param settlementStatus 结算状态
     * @return 执行单列表
     */
    List<ActualOrdersPo> listBySettlementStatus(int settlementStatus);
}

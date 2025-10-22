package com.studio.settlement.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.ServersPo;
import com.baomidou.mybatisplus.extension.service.IService;
import com.studio.settlement.bean.vo.ServerVO;
import com.studio.settlement.bean.vo.ServerStatisticsVO;

import java.util.List;

/**
 * <p>
 * 打手基本信息表（简化版） 服务类
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
public interface ServersService extends IService<ServersPo> {

    /**
     * 根据用户名获取打手信息
     */
    ServerVO getByUsername(String username);

    /**
     * 根据状态获取打手列表
     */
    List<ServerVO> listByStatus(Integer status);

    /**
     * 根据在线状态获取打手列表
     */
    List<ServerVO> listByOnlineStatus(Integer onlineStatus);

    /**
     * 分页查询打手信息
     */
    IPage<ServerVO> pageServers(Page<ServersPo> page);

    /**
     * 更新打手状态
     */
    boolean updateStatus(Long id, Integer status);

    /**
     * 更新在线状态
     */
    boolean updateOnlineStatus(Long id, Integer onlineStatus);

    /**
     * 重置密码
     */
    boolean resetPassword(Long id, String newPassword);

    /**
     * 批量更新状态
     */
    boolean batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 获取打手统计信息
     */
    ServerStatisticsVO getServerStatistics();

    ServerVO convertToVO(ServersPo serversPo);
}

package com.studio.settlement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.ServersPo;
import com.studio.settlement.bean.vo.ServerVO;
import com.studio.settlement.bean.vo.ServerStatisticsVO;
import com.studio.settlement.mapper.ServersMapper;
import com.studio.settlement.service.ServersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 打手基本信息表（简化版） 服务实现类
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@Service
public class ServersServiceImpl extends ServiceImpl<ServersMapper, ServersPo> implements ServersService {

    @Override
    public ServerVO getByUsername(String username) {
        ServersPo server = getOne(new LambdaQueryWrapper<ServersPo>()
                .eq(ServersPo::getUsername, username));
        return convertToVO(server);
    }

    @Override
    public List<ServerVO> listByStatus(Integer status) {
        List<ServersPo> servers = list(new LambdaQueryWrapper<ServersPo>()
                .eq(ServersPo::getServerStatus, status));
        return servers.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public List<ServerVO> listByOnlineStatus(Integer onlineStatus) {
        List<ServersPo> servers = list(new LambdaQueryWrapper<ServersPo>()
                .eq(ServersPo::getOnlineStatus, onlineStatus));
        return servers.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<ServerVO> pageServers(Page<ServersPo> page) {
        IPage<ServersPo> serversPage = page(page, new LambdaQueryWrapper<ServersPo>()
                .orderByDesc(ServersPo::getCreateTime));

        return serversPage.convert(this::convertToVO);
    }

    @Override
    public boolean updateStatus(Long id, Integer status) {
        ServersPo server = new ServersPo();
        server.setId(id);
        server.setServerStatus(status.byteValue());
        return updateById(server);
    }

    @Override
    public boolean updateOnlineStatus(Long id, Integer onlineStatus) {
        ServersPo server = new ServersPo();
        server.setId(id);
        server.setOnlineStatus(onlineStatus.byteValue());
        return updateById(server);
    }

    @Override
    public boolean resetPassword(Long id, String newPassword) {
        ServersPo server = new ServersPo();
        server.setId(id);
        server.setPasswordHash(newPassword); // 实际项目中应该加密
        return updateById(server);
    }

    @Override
    public boolean batchUpdateStatus(List<Long> ids, Integer status) {
        ServersPo server = new ServersPo();
        server.setServerStatus(status.byteValue());

        return update(server, new LambdaUpdateWrapper<ServersPo>()
                .in(ServersPo::getId, ids));
    }

    @Override
    public ServerStatisticsVO getServerStatistics() {
        List<ServersPo> servers = list();

        ServerStatisticsVO statistics = new ServerStatisticsVO();
        statistics.setTotalServers(servers.size());

        int activeServers = 0;
        int frozenServers = 0;
        int onlineServers = 0;
        int offlineServers = 0;

        for (ServersPo server : servers) {
            if (server.getServerStatus() == 1) {
                activeServers++;
            } else if (server.getServerStatus() == 2) {
                frozenServers++;
            }

            if (server.getOnlineStatus() == 1) {
                onlineServers++;
            } else {
                offlineServers++;
            }
        }

        statistics.setActiveServers(activeServers);
        statistics.setFrozenServers(frozenServers);
        statistics.setOnlineServers(onlineServers);
        statistics.setOfflineServers(offlineServers);

        return statistics;
    }

    /**
     * 将PO转换为VO
     */
    @Override
    public ServerVO convertToVO(ServersPo server) {
        if (server == null) {
            return null;
        }

        ServerVO vo = new ServerVO();
        vo.setId(server.getId());
        vo.setServerCode(server.getServerCode());
        vo.setUsername(server.getUsername());
        vo.setRealName(server.getRealName());
        vo.setPhone(server.getPhone());
        vo.setServerStatus(server.getServerStatus());
        vo.setOnlineStatus(server.getOnlineStatus());
        vo.setTotalOrders(server.getTotalOrders());
        vo.setCompletedOrders(server.getCompletedOrders());
        vo.setTotalIncome(server.getTotalIncome());
        vo.setLastLoginTime(server.getLastLoginTime());
        vo.setCreateTime(server.getCreateTime());
//        vo.setUpdateTime(server.getUpdateTime());

        return vo;
    }
}

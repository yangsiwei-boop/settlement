package com.studio.settlement.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.studio.settlement.bean.po.ServersPo;
import com.studio.settlement.bean.response.ApiResult;
import com.studio.settlement.bean.vo.ServerVO;
import com.studio.settlement.bean.vo.ServerStatisticsVO;
import com.studio.settlement.service.ServersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 打手基本信息表（简化版） 前端控制器
 * </p>
 *
 * @author YourName
 * @since 2025-10-22
 */
@RestController
@RequestMapping("/settlement/servers")
@Tag(name = "打手基本信息管理", description = "打手基本信息的增删改查接口")
public class ServersController {

    @Autowired
    private ServersService serversService;

    @Operation(summary = "创建打手信息", description = "创建一个新的打手信息")
    @PostMapping
    public ApiResult<ServerVO> createServer(@RequestBody ServersPo serversPo) {
        boolean success = serversService.save(serversPo);
        if (success) {
            ServerVO serverVO = serversService.convertToVO(serversPo);
            return ApiResult.ok("创建成功").data(serverVO);
        }
        return ApiResult.error("创建失败");
    }

    @Operation(summary = "更新打手信息", description = "更新指定的打手信息")
    @PostMapping("/updateServer/{id}")
    public ApiResult<ServerVO> updateServer(
            @Parameter(description = "打手ID") @PathVariable Long id,
            @RequestBody ServersPo serversPo) {
        serversPo.setId(id);
        boolean success = serversService.updateById(serversPo);
        if (success) {
            ServerVO serverVO = serversService.convertToVO(serversPo);
            return ApiResult.ok("更新成功").data(serverVO);
        }
        return ApiResult.error("更新失败");
    }

    @Operation(summary = "删除打手信息", description = "删除指定的打手信息")
    @PostMapping("/deleteServer/{id}")
    public ApiResult<Void> deleteServer(@Parameter(description = "打手ID") @PathVariable Long id) {
        boolean success = serversService.removeById(id);
        if (success) {
            return ApiResult.ok("删除成功");
        }
        return ApiResult.error("极速版删除失败");
    }

    @Operation(summary = "批量删除打手信息", description = "批量删除打手信息")
    @PostMapping("/batch")
    public ApiResult<Void> batchDeleteServers(@Parameter(description = "打手ID列表") @RequestBody List<Long> ids) {
        boolean success = serversService.removeByIds(ids);
        if (success) {
            return ApiResult.ok("批量删除成功");
        }
        return ApiResult.error("批量删除失败");
    }

    @Operation(summary = "获取打手详情", description = "根据ID获取打手的详细信息")
    @GetMapping("/{id}")
    public ApiResult<ServerVO> getById(@Parameter(description = "打手ID") @PathVariable Long id) {
        ServersPo server = serversService.getById(id);
        if (server != null) {
            ServerVO serverVO = serversService.convertToVO(server);
            return ApiResult.ok("查询成功").data(serverVO);
        }
        return ApiResult.error("打手不存在");
    }

    @Operation(summary = "分页查询打手信息", description = "极速版分页查询所有打手信息")
    @GetMapping("/page")
    public ApiResult<IPage<ServerVO>> page(
            @Parameter(description = "当前页码") @RequestParam(defaultValue = "1") Integer current,
            @Parameter(description = "每页大小") @RequestParam(defaultValue = "10") Integer size) {
        Page<ServersPo> page = new Page<>(current, size);
        IPage<ServerVO> result = serversService.pageServers(page);
        return ApiResult.ok("查询成功").data(result);
    }

    @Operation(summary = "获取所有打手信息", description = "获取所有打手信息列表")
    @GetMapping("/list")
    public ApiResult<List<ServerVO>> list() {
        List<ServersPo> servers = serversService.list();
        List<ServerVO> serverVOs = servers.stream()
                .map(serversService::convertToVO)
                .collect(Collectors.toList());
        return ApiResult.ok("查询成功").data(serverVOs);
    }

    @Operation(summary = "根据状态查询打手", description = "根据状态查询打手信息")
    @GetMapping("/status/{status}")
    public ApiResult<List<ServerVO>> getByStatus(
            @Parameter(description = "状态 (1:正常 2:冻结)") @PathVariable Integer status) {
        List<ServerVO> servers = serversService.listByStatus(status);
        return ApiResult.ok("查询成功").data(servers);
    }

    @Operation(summary = "根据在线状态查询打手", description = "根据在线状态查询打手信息")
    @GetMapping("/online-status/{onlineStatus}")
    public ApiResult<List<ServerVO>> getByOnlineStatus(
            @Parameter(description = "在线状态 (0:离线 1:在线)") @PathVariable Integer onlineStatus) {
        List<ServerVO> servers = serversService.listByOnlineStatus(onlineStatus);
        return ApiResult.ok("查询成功").data(servers);
    }

    @Operation(summary = "根据用户名查询打手", description = "根据用户名查询打手信息")
    @GetMapping("/username/{username}")
    public ApiResult<ServerVO> getByUsername(
            @Parameter(description = "用户名") @PathVariable String username) {
        ServerVO server = serversService.getByUsername(username);
        if (server != null) {
            return ApiResult.ok("查询成功").data(server);
        }
        return ApiResult.error("打手不存在");
    }

    @Operation(summary = "更新打手状态", description = "更新打手的账号状态")
    @PostMapping("/{id}/status")
    public ApiResult<Void> updateStatus(
            @Parameter(description = "打手ID") @PathVariable Long id,
            @Parameter(description = "状态 (1:正常 2:冻结)") @RequestParam Integer status) {
        boolean success = serversService.updateStatus(id, status);
        if (success) {
            return ApiResult.ok("状态更新成功");
        }
        return ApiResult.error("状态更新失败");
    }

    @Operation(summary = "更新在线状态", description = "更新打手的在线状态")
    @PostMapping("/{id}/online-status")
    public ApiResult<Void> updateOnlineStatus(
            @Parameter(description = "打手ID") @PathVariable Long id,
            @Parameter(description = "在线状态 (0:离线 1:在线)") @RequestParam Integer onlineStatus) {
        boolean success = serversService.updateOnlineStatus(id, onlineStatus);
        if (success) {
            return ApiResult.ok("在线状态更新成功");
        }
        return ApiResult.error("在线状态更新失败");
    }

    @Operation(summary = "重置打手密码", description = "重置打手的登录密码")
    @PostMapping("/{id}/reset-password")
    public ApiResult<Void> resetPassword(
            @Parameter(description = "打手ID") @PathVariable Long id,
            @Parameter(description = "新密码") @RequestParam String newPassword) {
        boolean success = serversService.resetPassword(id, newPassword);
        if (success) {
            return ApiResult.ok("密码重置成功");
        }
        return ApiResult.error("密码重置失败");
    }

    @Operation(summary = "获取打手统计信息", description = "获取打手的统计信息")
    @GetMapping("/statistics")
    public ApiResult<ServerStatisticsVO> getStatistics() {
        ServerStatisticsVO statistics = serversService.getServerStatistics();
        return ApiResult.ok("查询成功").data(statistics);
    }
}

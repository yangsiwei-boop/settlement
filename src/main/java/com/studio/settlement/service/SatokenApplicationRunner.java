package com.studio.settlement.service;

import cn.dev33.satoken.stp.SaLoginModel;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@Order(1)
public class SatokenApplicationRunner implements ApplicationRunner {
    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 支持解析键值对参数（如 --key=value）
        Map<String, SaTokenInfo> tokenMap = redisTemplate.opsForHash().entries("saTokenMap");
        int tokenSize = 0;
        if(null != tokenMap){
            // 遍历tokenMap
            tokenMap.forEach((userId, tokenInfo) -> {
                // 使用正确的登录配置类 SaLoginModel (v1.32+)
                SaLoginModel loginModel = new SaLoginModel()
                        .setDevice("default")
                        .setToken(tokenInfo.getTokenValue());

                // 直接使用StpUtil替代StpLogic
                StpUtil.createLoginSession(
                        Long.parseLong(userId), // 正确转换为Long
                        loginModel             // 使用SaLoginModel实例
                );
            });
            tokenSize = tokenMap.size();
        }
         // 检查--debug是否存在
        log.info("SatokenApplicationRunner执行, token数: " + tokenSize);
    }
}

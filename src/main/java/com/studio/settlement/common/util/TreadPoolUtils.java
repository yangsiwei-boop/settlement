package com.studio.settlement.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Component
@Slf4j
public class TreadPoolUtils {

    @Bean
    public Executor asyncServiceExecutor() {
        log.info("start asyncServiceExecutor");
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(15);
        //配置最大线程数
        executor.setMaxPoolSize(30);
        executor.setKeepAliveSeconds(60);
        //配置队列大小
        executor.setQueueCapacity(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("sxd-async-service-");
        // 设置拒绝策略：当pool已经达到max size的时候，如何处理新任务
        // CALLER_RUNS：不在新线程中执行任务，而是有调用者所在的线程来执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        //执行初始化
        executor.initialize();
        return executor;
    }
}

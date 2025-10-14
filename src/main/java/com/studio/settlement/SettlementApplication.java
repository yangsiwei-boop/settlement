package com.studio.settlement;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@MapperScan("com.studio.settlement.mapper")
@SpringBootApplication
@EnableScheduling
@EnableAsync(proxyTargetClass=true )
public class SettlementApplication {

    public static void main(String[] args) {
        log.info("SettlementApplication started");
        SpringApplication.run(SettlementApplication.class, args);
    }

}

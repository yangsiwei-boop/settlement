package com.studio.settlement.generator;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.VelocityTemplateEngine;

import java.util.Collections;

public class CodeGenerator {

    // 数据库配置
    private static final String JDBC_URL = "jdbc:mysql://114.67.212.224:3306/settlement?serverTimezone=GMT%2B8&useUnicode=true&characterEncoding=utf8&autoReconnect=true&allowMultiQueries=true&useSSL=false&rewriteBatchedStatements=true";
    private static final String JDBC_USERNAME = "root";
    private static final String JDBC_PASSWORD = "e^7I5Qfo!Z=s";

    // 项目配置
    private static final String PROJECT_PATH = System.getProperty("user.dir");
    private static final String PARENT_PACKAGE = "com.studio.settlement";

    // 表配置
    private static final String[] TABLE_NAMES = {"yb_base_orders", "yb_actual_orders"};
    private static final String TABLE_PREFIX = "yb_";

    public static void main(String[] args) {
        System.out.println("项目路径: " + PROJECT_PATH);
        System.out.println("生成表: " + String.join(", ", TABLE_NAMES));

        FastAutoGenerator.create(JDBC_URL, JDBC_USERNAME, JDBC_PASSWORD)
                .globalConfig(builder -> {
                    builder.author("YourName")
                            .outputDir(PROJECT_PATH + "/src/main/java")
                            .dateType(DateType.ONLY_DATE)
                            .disableOpenDir()
                            .commentDate("yyyy-MM-dd");
                })
                .packageConfig(builder -> {
                    builder.parent(PARENT_PACKAGE)
                            .entity("bean.po")
                            .mapper("mapper")
                            .service("service")
                            .serviceImpl("service.impl")
                            .controller("controller");
                })
                .strategyConfig(builder -> {
                    builder.addInclude(TABLE_NAMES)
                            .addTablePrefix(TABLE_PREFIX)

                            // 实体类策略
                            .entityBuilder()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableFileOverride()
                            .formatFileName("%sPo")
                            .disableSerialVersionUID()
                            .idType(IdType.AUTO)
                            .columnNaming(NamingStrategy.underline_to_camel)

                            // Mapper策略
                            .mapperBuilder()
                            .enableBaseResultMap()
                            .enableBaseColumnList()
                            .enableFileOverride()
                            .formatMapperFileName("%sMapper")
                            .formatXmlFileName("%sMapper")

                            // Service策略
                            .serviceBuilder()
                            .formatServiceFileName("%sService")
                            .formatServiceImplFileName("%sServiceImpl")
                            .enableFileOverride()

                            // Controller策略 - 使用Springdoc注解
                            .controllerBuilder()
                            .enableRestStyle()
                            .enableFileOverride()
                            .enableHyphenStyle()
                            .formatFileName("%sController");
                })
                .templateConfig(builder -> {
                    builder.entity("/templates/entity.java.vm");
                })
                .templateEngine(new VelocityTemplateEngine())
                .execute();

        System.out.println("代码生成完成！");
        System.out.println("实体类位置: " + PARENT_PACKAGE + ".po");
    }
}

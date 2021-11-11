package com.salk.lib.practice.delaycheck.callback.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.salk.lib.practice.delaycheck.callback.support.DruidDataSourceProperties;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.sql.SQLException;


@Slf4j
@Configuration
@MapperScan(basePackages = {"com.salk.lib.practice.delaycheck.mapper"})
@EnableConfigurationProperties(value = DruidDataSourceProperties.class)
public class SpringMyBatisConfig {

    @Autowired
    private DruidDataSourceProperties druidDataSourceProperties;

    @Bean
    public DataSource dataSource() throws SQLException {
        System.out.println(druidDataSourceProperties);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUsername(druidDataSourceProperties.getUsername());
        druidDataSource.setPassword(druidDataSourceProperties.getPassword());
        druidDataSource.setUrl(druidDataSourceProperties.getJdbcUrl());
        druidDataSource.setDriverClassName(druidDataSourceProperties.getDriverClassName());
        druidDataSource.setInitialSize(druidDataSourceProperties.getInitialSize());
        druidDataSource.setMinIdle(druidDataSourceProperties.getMinIdle());
        druidDataSource.setMaxActive(druidDataSourceProperties.getMaxActive());
        druidDataSource.setMaxWait(druidDataSourceProperties.getMaxWait());
        druidDataSource.setTestOnBorrow(true);
        druidDataSource.setTestOnReturn(true);
        druidDataSource.setFilters(druidDataSourceProperties.getFilters());
        druidDataSourceProperties.setPoolPreparedStatements(druidDataSourceProperties.isPoolPreparedStatements());
        return druidDataSource;
    }


    @Bean
    public PlatformTransactionManager transactionManager() throws SQLException {
        PlatformTransactionManager transactionManager = new DataSourceTransactionManager(dataSource());
        return transactionManager;
    }
}

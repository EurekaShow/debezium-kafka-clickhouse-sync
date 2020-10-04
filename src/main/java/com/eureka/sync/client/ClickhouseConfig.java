package com.eureka.sync.client;

import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@Configuration
public class ClickhouseConfig {

    @Value("${spring.datasource.click.driverClassName}")
    private String driverClassName ;
    @Value("${spring.datasource.click.url}")
    private String url ;

    @Value("${spring.datasource.click.userName}")
    private String userName ;
    @Value("${spring.datasource.click.password}")
    private String password ;
    @Value("${spring.datasource.click.initialSize}")
    private Integer initialSize ;
//    @Value("${spring.datasource.click.timeOut}")
//    private Integer timeOut ;
    @Value("${spring.datasource.click.maxActive}")
    private Integer maxActive ;
    @Value("${spring.datasource.click.minIdle}")
    private Integer minIdle ;
    @Value("${spring.datasource.click.maxWait}")
    private Integer maxWait ;

    @Bean
    public DataSource dataSource() {
        DruidDataSource datasource = new DruidDataSource();
        datasource.setUrl(url);
        datasource.setDriverClassName(driverClassName);
        datasource.setInitialSize(initialSize);
        datasource.setMinIdle(minIdle);
        datasource.setMaxActive(maxActive);
        datasource.setMaxWait(maxWait);
        datasource.setUsername(userName);
        datasource.setPassword(password);
//        datasource.setQueryTimeout();
        return datasource;
    }
}

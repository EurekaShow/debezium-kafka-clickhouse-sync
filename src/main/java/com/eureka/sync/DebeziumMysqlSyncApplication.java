package com.eureka.sync;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@SpringBootApplication
public class DebeziumMysqlSyncApplication {
    public static void main(String[] args) {
        SpringApplication.run(DebeziumMysqlSyncApplication.class, args);
    }
}
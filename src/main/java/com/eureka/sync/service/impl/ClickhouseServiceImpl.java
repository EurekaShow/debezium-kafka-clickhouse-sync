package com.eureka.sync.service.impl;

import com.eureka.sync.service.ClickhouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@Service
public class ClickhouseServiceImpl implements ClickhouseService {
    private static final Logger logger = LoggerFactory.getLogger(ClickhouseServiceImpl.class);

    @Autowired
    public JdbcTemplate jdbcTemplate;

    @Override
    public void insert(String sql,Object... params)  {
        try {
            jdbcTemplate.update(sql,params);

        } catch (Throwable e) {
            logger.error("Clickhouse插入错误, INSERT SQL=" + sql, e);
//            throw e;
        }

    }

    @Override
    public void update(String sql,Object... params) {
        try {
            jdbcTemplate.update(sql,params);

        } catch (Throwable e) {
            logger.error("Clickhouse更新错误, SQL=" + sql, e);
//            throw e;
        }
    }

    @Override
    public void deleteById(String sql, Object... params) {

        try {
            jdbcTemplate.update(sql,params);

        } catch (Throwable e) {
            logger.error("Clickhouse删除错误, SQL=" + sql, e);
//            throw e;
        }
    }
}

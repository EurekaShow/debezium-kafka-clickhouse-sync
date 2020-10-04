package com.eureka.sync.service;

import io.searchbox.client.JestResult;

import java.util.Map;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public interface ClickhouseService {
    void insert(String sql,Object... params);

    void update(String sql,Object... params);

    void deleteById(String sql,Object... params);
}

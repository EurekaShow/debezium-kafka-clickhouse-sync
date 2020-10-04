package com.eureka.sync.service;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public interface MappingService {

    /**
     * 获取Elasticsearch的数据转换后类型
     *
     * @param kfType kafka数据类型
     * @param data      具体数据
     * @return Elasticsearch对应的数据类型
     */
    Object getClickhoseTypeObject(String kftype,String dbtype,  String data);
}

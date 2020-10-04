package com.eureka.sync.listener;

import com.eureka.sync.event.InsertDmlEvent;
import com.eureka.sync.service.ClickhouseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@Component
public class InsertDmlListener extends AbstractDmlListener<InsertDmlEvent> {
    private static final Logger logger = LoggerFactory.getLogger(InsertDmlListener.class);

    @Resource
    private ClickhouseService clickhouseService;

    @Override
    protected void doSync(String database, String table, String body) {
        String sql_fix = parseColumnsToInsert(body);
        String sql = "insert into ".concat(database).concat(".").concat(table).concat(sql_fix);
        Object[] paras = parseColumnsParams(body);
        clickhouseService.insert(sql,paras);
        logger.debug("insert_ch_info 同步ch插入操作成功！database=" + database + ",table=" + table );
    }
}

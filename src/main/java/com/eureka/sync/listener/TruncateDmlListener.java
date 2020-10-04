package com.eureka.sync.listener;

import com.eureka.sync.event.TruncateDmlEvent;
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
public class TruncateDmlListener extends AbstractDmlListener<TruncateDmlEvent> {
    private static final Logger logger = LoggerFactory.getLogger(TruncateDmlListener.class);

    @Resource
    private ClickhouseService clickhouseService;

    @Override
    protected void doSync(String database, String table, String rowData) {

        String sql = "ALTER TABLE ".concat(database).concat(".").concat(table).concat(" delete WHERE ").concat(" 1=1");

        clickhouseService.deleteById(sql);

        logger.debug("DELETE 同步ch操作成功！database=" + database + ",table=" + table );
    }
}

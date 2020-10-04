package com.eureka.sync.listener;

import com.eureka.sync.event.DeleteDmlEvent;
import com.eureka.sync.service.ClickhouseService;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Optional;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@Component
public class DeleteDmlListener extends AbstractDmlListener<DeleteDmlEvent> {
    private static final Logger logger = LoggerFactory.getLogger(DeleteDmlListener.class);

    @Resource
    private ClickhouseService clickhouseService;

    @Override
    protected void doSync(String database, String table, String rowData) {

        Optional<String> idColumn = Optional.ofNullable(getFirstPK(rowData));

        if (!idColumn.isPresent() || StringUtils.isBlank(idColumn.get())) {
            logger.warn("DELETE 从column中找不到主键,database=" + database + ",table=" + table);
            return;
        }

        String sql = "ALTER TABLE ".concat(database).concat(".").concat(table).concat(" delete WHERE ").concat(idColumn.get()).concat(" =?");
        String val = ((Map)JsonPath.read(rowData, "$.payload.before")).get(idColumn.get()).toString();
        clickhouseService.deleteById(sql,val);

        logger.debug("DELETE 同步ch操作成功！database=" + database + ",table=" + table + ",id=" + idColumn.get());
    }
}

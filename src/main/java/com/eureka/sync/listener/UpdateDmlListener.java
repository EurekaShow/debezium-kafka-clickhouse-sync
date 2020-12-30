package com.eureka.sync.listener;

import com.eureka.sync.event.UpdateDmlEvent;
import com.eureka.sync.service.ClickhouseService;
import com.jayway.jsonpath.JsonPath;
//import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.util.Strings;
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
public class UpdateDmlListener extends AbstractDmlListener<UpdateDmlEvent> {
    private static final Logger logger = LoggerFactory.getLogger(UpdateDmlListener.class);

    @Resource
    private ClickhouseService clickhouseService;

    @Override
    protected void doSync(String database, String table, String rowData) {

        Optional<String> idColumn = Optional.ofNullable(getFirstPK(rowData));
        if (!idColumn.isPresent() || Strings.isBlank(idColumn.get())) {
            logger.warn("update从column中找不到主键,database=" + database + ",table=" + table);
            return;
        }

        String del_sql = "ALTER TABLE ".concat(database).concat(".").concat(table).concat(" delete WHERE ").concat(idColumn.get()).concat(" =?");
        String id_val = ((Map) JsonPath.read(rowData, "$.payload.after")).get(idColumn.get()).toString();
        clickhouseService.deleteById(del_sql,id_val);

        String sql = "insert into ".concat(database).concat(".").concat(table).concat(parseColumnsToInsert(rowData));
        clickhouseService.insert(sql,parseColumnsParams(rowData));

        logger.debug("update_es_info 同步es插入操作成功！database=" + database + ",table=" + table);
    }
}

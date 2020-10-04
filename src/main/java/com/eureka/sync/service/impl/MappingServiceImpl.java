package com.eureka.sync.service.impl;

import com.eureka.sync.service.MappingService;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
@Service
public class MappingServiceImpl implements MappingService, InitializingBean {
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private Map<String, Converter> kfTypeMapping;

    @Override
    public Object getClickhoseTypeObject(String kftype,String dbtype, String data) {
        System.out.println(data);
        if(dbtype==null || !dbtype.equals("Timestamp")) {
            Optional<Entry<String, Converter>> result = kfTypeMapping.entrySet().parallelStream().filter(
                    entry -> kftype.toLowerCase().contains(entry.getKey())
            ).findFirst();
            return (result.isPresent() ? result.get().getValue() : (Converter) data1 -> data1).convert(data);
        }else{
            return formatter.format(new Date(Long.parseLong(data)));
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        kfTypeMapping = Maps.newHashMap();
        kfTypeMapping.put("string", data -> data);
        kfTypeMapping.put("int32", Integer::valueOf);
        kfTypeMapping.put("int64", Long::valueOf);
        kfTypeMapping.put("bytes", Double::valueOf);
    }

    private interface Converter {
        Object convert(String data);
    }

}

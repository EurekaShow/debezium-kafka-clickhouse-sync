package com.eureka.sync.listener;

import com.eureka.sync.service.MappingService;
import com.eureka.sync.event.DmlEvent;
import com.google.common.base.Strings;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
//import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author eureka
 * @version 1.0
 * @since 2020-10-03
 */
public abstract class AbstractDmlListener<EVENT extends DmlEvent> implements ApplicationListener<EVENT> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractDmlListener.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    @Resource
    private MappingService mappingService;

    @Override
    public void onApplicationEvent(EVENT event) {

        String entry = event.getEntry().toString();

        String database;
        String table;

        if(new JSONObject(entry).has("ddl")){

            database = JsonPath.read(entry, "$.databaseName");
            table = JsonPath.read(entry, "$.ddl").toString().split(" ")[1].replaceAll("`","");

        }else{

            database = JsonPath.read(entry, "$.payload.source.db");
            table = JsonPath.read(entry, "$.payload.source.table");

        }

        doSync(database, table,entry);
    }

    public String getFirstPK(String body) {
        if (body != null && !body.isEmpty()) {

            System.out.println("Received message: " + body);

            Map after = JsonPath.read(body, "$.payload.before");

            if (after != null) {
                JSONArray struct = JsonPath.read(body, "$.schema.fields[1].fields");
                for (Iterator<Object> it = struct.iterator(); it.hasNext(); ) {

                    Map fields = (Map) it.next();
                    if(fields.get("optional").toString().equals("false")){
                        return fields.get("field").toString();
                    }
                }
            }
        }
        return null;
    }


    public String parseColumnsToInsert(String body) {

        if (body != null && !body.isEmpty()) {

            System.out.println("Received message: " + body);

            Map after = JsonPath.read(body, "$.payload.after");

            if (after != null) {
                JSONArray struct = JsonPath.read(body, "$.schema.fields[1].fields");

                StringBuilder keys = new StringBuilder();
                StringBuilder values = new StringBuilder();
                keys.append(" (");
                values.append(" VALUES(");

                for (Iterator<Object> it = struct.iterator(); it.hasNext(); ) {

                    Map fields = (Map) it.next();

                    String key = fields.get("field").toString();
                    if (key == null || Strings.isNullOrEmpty(key)) {
                        continue;
                    }
                    keys.append(",").append(key);
                    values.append(",").append("?");
                }

                String sql = keys.append(")").toString().replaceFirst(",", "").concat(values.append(")").toString().replaceFirst(",", ""));

                return sql;
            } else {
                return null;
            }
        }
        return null;
    }

    public Object[] parseColumnsParams(String body) {

        if (body != null && !body.isEmpty()) {

            System.out.println("Received message: " + body);

            Map after = JsonPath.read(body, "$.payload.after");

            if (after != null) {
                JSONArray struct = JsonPath.read(body, "$.schema.fields[1].fields");
                ArrayList<Object> params = new ArrayList<>();

                for (Iterator<Object> it = struct.iterator(); it.hasNext(); ) {

                    Map fields = (Map) it.next();

                    String key = fields.get("field").toString();

                    Object dbtype =fields.get("name");

                    String dbtp = dbtype==null || (!dbtype.toString().toLowerCase().contains("time")
                            && !dbtype.toString().toLowerCase().contains("date"))?null:"date";

//                    String kftype = fields.get("type").toString();

                    String val = after.get(key)==null?null:after.get(key).toString();
                    if(dbtp !=null && dbtp.equals("date") && val!=null){

                        Calendar cal = Calendar.getInstance();
                        cal.setTime(new Date(Long.parseLong(val)));
                        cal.add(Calendar.HOUR_OF_DAY, -16);

                        val = formatter.format(cal.getTime());
                        logger.info(val);
                    }
//                    if (key == null) {
//                        continue;
//                    }
//                    Object keyvalue = val == null
//                            || StringUtils.isBlank(val)
//                            || val.toLowerCase() == "null"
//                            ? null
//                            : mappingService.getClickhoseTypeObject(kftype,dbtp, val);
                    params.add(val);

                }
                return params.toArray();

//            ((LinkedHashMap) struct.get(0)).get("field")//type
            } else {
                System.out.println("After changes...");
                System.out.println("null");
                return null;
            }
        }
        return null;
    }




    protected abstract void doSync(String database, String table, String rowData);
}

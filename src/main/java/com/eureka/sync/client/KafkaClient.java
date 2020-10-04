package com.eureka.sync.client;

import com.eureka.sync.event.DeleteDmlEvent;
import com.eureka.sync.event.InsertDmlEvent;
import com.eureka.sync.event.TruncateDmlEvent;
import com.eureka.sync.event.UpdateDmlEvent;
import com.eureka.sync.listener.AbstractDmlListener;
import com.jayway.jsonpath.JsonPath;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;

@Component
public class KafkaClient  implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(AbstractDmlListener.class);
    private ApplicationContext applicationContext;

    @KafkaListener(topics = {"cosmo_lady_fmds_dev.DDL","cosmo_lady_fmds_dev.cosmo_lady_fmds_dev.allocation_provision_second_092701",
            "cosmo_lady_fmds_dev.cosmo_lady_fmds_dev.allocation_provision_second_092701_copy1"})
    public void listen(ConsumerRecord<?, ?> record) throws Exception {

        try {

            if(record.value() == null || StringUtils.isEmpty(record.value().toString())){
//                ack.acknowledge();
                return;
            }
            JSONObject content = new JSONObject(record.value().toString());
            String eventType;

            if(content.has("ddl")){
                eventType = JsonPath.read(record.value().toString(), "$.ddl").toString().split(" ")[0].toUpperCase();
            }else{
                eventType= JsonPath.read(record.value().toString(), "$.payload.op").toString().toUpperCase();
            }

            switch (eventType) {
                case "C":
                    applicationContext.publishEvent(new InsertDmlEvent(record.value().toString()));
                    break;
                case "U":
                    applicationContext.publishEvent(new UpdateDmlEvent(record.value().toString()));
                    break;
                case "D":
                    applicationContext.publishEvent(new DeleteDmlEvent(record.value().toString()));
                    break;
                case "TRUNCATE":
                    applicationContext.publishEvent(new TruncateDmlEvent(record.value().toString()));
                default:
                    logger.info("donot do process,message: "+record.value().toString());
                    break;
            }
//            ack.acknowledge();
            System.out.println("receiver success");
        }catch (Throwable o){
            logger.error("receiver error,message: "+record.value().toString());
        }
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}


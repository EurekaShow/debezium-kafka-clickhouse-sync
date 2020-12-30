package com.eureka.sync.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfiguration implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(KafkaTopicsConfiguration.class);

    @Value("${kafka.listener.topics}")
    private String topic;
    @Override
    public void afterPropertiesSet() throws Exception {
        System.setProperty("topics", topic);
        logger.info("#########  system config topic:{} ########", topic);
    }

}

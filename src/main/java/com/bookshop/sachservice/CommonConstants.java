package com.bookshop.sachservice;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonConstants {
    public static String KAFKA_TOPIC_ORDER_PLACEMENT;

    @Value("${constant.kafka.order-placed}")
    public void setKafkaTopicOrderPlacement(String topic) {
        CommonConstants.KAFKA_TOPIC_ORDER_PLACEMENT = topic;
    }
}


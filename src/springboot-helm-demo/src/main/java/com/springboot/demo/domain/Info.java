package com.springboot.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info {
    private String app_version;
    private String environment;
    private DbConfig db_properties;
    private KafkaConsumer kafka_consumer;
}

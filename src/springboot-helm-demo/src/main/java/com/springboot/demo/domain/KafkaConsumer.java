package com.springboot.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KafkaConsumer {
    private String bootstrap_servers;
    private String group_id;
    private String key_deserializer;
    private String value_deserializer;
}

package com.springboot.demo.controllers;

import com.springboot.demo.domain.DbConfig;
import com.springboot.demo.domain.Info;
import com.springboot.demo.domain.KafkaConsumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
@Slf4j
@RequiredArgsConstructor
public class InfoController {

    @Value("${app.version}")
    private String appVersion;

    @GetMapping
    public ResponseEntity<Info> findAll() {
        Info info = new Info();
        info.setApp_version(appVersion);
        info.setDb_properties(new DbConfig());
        info.setKafka_consumer(new KafkaConsumer());

        //Set environment
        String env = System.getenv().getOrDefault("DEPLOY_ENV", "none");
        info.setEnvironment(env);

        //Set db_conf
        String db_conf= System.getenv().getOrDefault("DB_CONF", "none");
        info.getDb_properties().setDb_conf(db_conf);

        //Set db_user
        String db_user= System.getenv().getOrDefault("DB_USER", "none");
        info.getDb_properties().setDb_user(db_user);

        //Set Kafka Consumer properties
        String servers= System.getenv().getOrDefault("BOOTSTRAP_SERVERS", "none");
        info.getKafka_consumer().setBootstrap_servers(servers);

        String group= System.getenv().getOrDefault("GROUP_ID", "none");
        info.getKafka_consumer().setGroup_id(group);

        String key_s= System.getenv().getOrDefault("KEY_DESERIALIZER", "none");
        info.getKafka_consumer().setKey_deserializer(key_s);

        String value_s= System.getenv().getOrDefault("VALUE_DESERIALIZER", "none");
        info.getKafka_consumer().setValue_deserializer(value_s);

        return ResponseEntity.ok(info);
    }
}
package com.springboot.demo.controllers;

import com.springboot.demo.domain.DbConfig;
import com.springboot.demo.domain.Info;
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

        //Set environment
        String env = System.getenv().getOrDefault("DEPLOY_ENV", "none");
        info.setEnvironment(env);

        //Set db_conf
        String db_conf= System.getenv().getOrDefault("DB_CONF", "none");
        info.getDb_properties().setDb_conf(db_conf);

        //Set db_user
        String db_user= System.getenv().getOrDefault("DB_USER", "none");
        info.getDb_properties().setDb_user(db_user);

        return ResponseEntity.ok(info);
    }
}
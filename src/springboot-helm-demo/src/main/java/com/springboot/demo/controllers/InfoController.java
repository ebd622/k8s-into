package com.springboot.demo.controllers;

import com.springboot.demo.domain.Info;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/info")
@Slf4j
@RequiredArgsConstructor
public class InfoController {

    @GetMapping
    public ResponseEntity<Info> findAll() {
        Info info = new Info();
        info.setDatabase("Test");
        info.setVersion("1");
        return ResponseEntity.ok(info);
    }
}
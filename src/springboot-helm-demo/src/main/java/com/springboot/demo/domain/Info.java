package com.springboot.demo.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Info {
    private String app_version;
    private String environment;
    private String db_conf;
    private String db_user;
}

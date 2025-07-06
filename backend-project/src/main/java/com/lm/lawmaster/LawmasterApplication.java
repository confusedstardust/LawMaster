package com.lm.lawmaster;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lm.lawmaster.dao")
public class LawmasterApplication {

    public static void main(String[] args) {
        SpringApplication.run(LawmasterApplication.class, args);
    }

}

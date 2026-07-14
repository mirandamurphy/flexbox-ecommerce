package com.flexbox.backend;

import org.springframework.boot.SpringApplication;

public class TestFlexboxBackendApplication {

    public static void main(String[] args) {
        SpringApplication.from(FlexboxBackendApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}

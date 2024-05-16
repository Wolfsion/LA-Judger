package com.lavson.laojcodesandbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class LaojCodeSandboxApplication {

    public static void main(String[] args) {
        SpringApplication.run(LaojCodeSandboxApplication.class, args);
    }

}

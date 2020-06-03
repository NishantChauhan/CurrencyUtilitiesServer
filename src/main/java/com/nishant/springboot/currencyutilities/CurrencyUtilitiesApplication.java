package com.nishant.springboot.currencyutilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@EnableFeignClients
public class CurrencyUtilitiesApplication {

    public static void main(String[] args) {

        String ENV_PORT = System.getenv().get("PORT");
        String ENV_DYNO = System.getenv().get("DYNO");

        if (ENV_PORT != null && ENV_DYNO != null) {
            System.getProperties().put("server.port", ENV_PORT);
        }
        SpringApplication.run(CurrencyUtilitiesApplication.class, args);
    }
}

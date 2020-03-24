package com.nishant.springboot.currencyutilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jackson.JacksonAutoConfiguration;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(exclude = {JacksonAutoConfiguration.class})
@EnableFeignClients
public class CurrencyUtilitiesApplication {

    public static void main(String[] args) {
        SpringApplication.run(CurrencyUtilitiesApplication.class, args);
    }

}

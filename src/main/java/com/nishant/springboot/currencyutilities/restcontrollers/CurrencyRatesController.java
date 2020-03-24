package com.nishant.springboot.currencyutilities.restcontrollers;

import com.nishant.springboot.currencyutilities.model.Rates;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/currency/rates")

public class CurrencyRatesController {
    CurrencyClientInterface currencyFeignClient;

    public CurrencyRatesController(CurrencyClientInterface currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rates getLatestRates() {
        return currencyFeignClient.getLatestRates(System.getenv("API_KEY"));
    }
}

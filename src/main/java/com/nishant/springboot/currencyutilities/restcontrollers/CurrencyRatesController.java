package com.nishant.springboot.currencyutilities.restcontrollers;

import com.nishant.springboot.currencyutilities.model.Currency;
import com.nishant.springboot.currencyutilities.model.Rates;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;


@RestController
@RequestMapping("/api/v1/currency/rates")

public class CurrencyRatesController {
    CurrencyClientInterface currencyFeignClient;

    public CurrencyRatesController(CurrencyClientInterface currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rates getLatestRates() {
        return currencyFeignClient.getLatestRates(System.getenv("API_KEY"));
    }

    @GetMapping(value = "/supportedCurrencies", produces = MediaType.APPLICATION_JSON_VALUE)
    public Set<Currency> getSupportedCurrencies() {
        Rates rates = currencyFeignClient.getLatestRates(System.getenv("API_KEY"));
        Set<Currency> supportedCurrencies = new TreeSet<>();
        if(rates !=null && rates.getRates()!=null && !rates.getRates().isEmpty()){
            Map<String, Double> currencyRates = rates.getRates();
            currencyRates.forEach((currencyName,currencyRate)->{
                Currency currency = new Currency(currencyName,currencyName);
                supportedCurrencies.add(currency);
            });
        }
        return supportedCurrencies;
    }

}

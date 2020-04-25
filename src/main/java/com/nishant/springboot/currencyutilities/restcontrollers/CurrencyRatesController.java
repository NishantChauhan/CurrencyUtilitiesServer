package com.nishant.springboot.currencyutilities.restcontrollers;

import com.nishant.springboot.currencyutilities.configuration.common.constants.CurrencyAPICommonUtilities;
import com.nishant.springboot.currencyutilities.model.Currency;
import com.nishant.springboot.currencyutilities.model.Rates;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;


@RestController
@RequestMapping("/api/v1/currency/rates")

public class CurrencyRatesController {
    CurrencyClientInterface currencyFeignClient;
    Logger logger = LoggerFactory.getLogger(this.getClass());
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
        Set<Currency> response = CurrencyAPICommonUtilities.getSupportedCurrencies(rates);
        logger.info(response.toString());
        return response;
    }
}

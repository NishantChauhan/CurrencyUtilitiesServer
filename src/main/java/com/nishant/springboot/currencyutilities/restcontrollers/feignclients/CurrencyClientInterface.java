package com.nishant.springboot.currencyutilities.restcontrollers.feignclients;

import com.nishant.springboot.currencyutilities.model.Rates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(url = "${currency.api.url}", name = "currencyClient")
public interface CurrencyClientInterface {

    @GetMapping("/latest")
    Rates getLatestRates(@RequestParam("access_key") String accessKey);

}

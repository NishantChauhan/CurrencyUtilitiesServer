package com.nishant.springboot.currencyutilities.local.restcontroller;

import com.nishant.springboot.currencyutilities.local.commonutils.RateUtils;
import com.nishant.springboot.currencyutilities.model.Rates;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Profile("dev")
@RestController
public class LatestRatesController {

    @SneakyThrows
    @GetMapping(value = "/latest", produces = MediaType.APPLICATION_JSON_VALUE)
    public Rates getLatestRates() {
        return RateUtils.getLatestRates();
    }
}

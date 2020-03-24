package com.nishant.springboot.currencyutilities.commonutils;

import com.google.gson.GsonBuilder;
import com.nishant.springboot.currencyutilities.model.Rates;
import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;
import java.io.FileReader;

public class RateUtils {

    public static Rates getLatestRates() throws FileNotFoundException {
        return (new GsonBuilder().setDateFormat("yyyy-MM-dd")).create().fromJson(
                new FileReader(ResourceUtils.getFile("classpath:samples/fixer/latestRates.json"))
                , Rates.class);
    }

}

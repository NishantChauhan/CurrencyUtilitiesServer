package com.nishant.springboot.currencyutilities.configuration.common.constants;

import com.nishant.springboot.currencyutilities.model.Currency;
import com.nishant.springboot.currencyutilities.model.Rates;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class CurrencyAPICommonUtilities {

    public static Set<Currency> getSupportedCurrencies(Rates rates) {
        Set<Currency> supportedCurrencies = new TreeSet<>();
        if (rates != null && rates.getRates() != null && !rates.getRates().isEmpty()) {
            Map<String, Double> currencyRates = rates.getRates();
            currencyRates.forEach((currencyName, currencyRate) -> {
                Currency currency = new Currency(currencyName, currencyName);
                supportedCurrencies.add(currency);
            });
        }
        return supportedCurrencies;
    }

}

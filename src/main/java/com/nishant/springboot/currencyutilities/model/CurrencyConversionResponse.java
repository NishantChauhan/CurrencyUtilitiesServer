package com.nishant.springboot.currencyutilities.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class CurrencyConversionResponse {
    String from, to;
    Date rateAsOf;
    Double amount, conversionRate, result;
    ResponseStatus responseStatus;
}

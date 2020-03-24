package com.nishant.springboot.currencyutilities.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyConversionResponse {
    String from, to;
    Double amount, result;
    ResponseStatus responseStatus;
}

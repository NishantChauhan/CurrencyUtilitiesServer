package com.nishant.springboot.currencyutilities.model.errorhandling;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CurrencyAPIErrorResponse {
    Integer code;
    String type;
    String info;
}

package com.nishant.springboot.currencyutilities.model.errorhandling;

import com.nishant.springboot.currencyutilities.model.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CurrencyAPIInputValidationException extends Exception {
    ResponseStatus responseStatus;
}

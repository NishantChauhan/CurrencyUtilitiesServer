package com.nishant.springboot.currencyutilities.model.errorhandling;

import com.nishant.springboot.currencyutilities.model.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CurrencyAPITechnicalException extends Exception {
    ResponseStatus responseStatus;

    public CurrencyAPITechnicalException(String message) {
        super(message);
    }

    public CurrencyAPITechnicalException(Throwable cause) {
        super(cause);
    }
}

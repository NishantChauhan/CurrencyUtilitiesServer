package com.nishant.springboot.currencyutilities.restcontrollers;

import com.nishant.springboot.currencyutilities.configuration.common.constants.CurrencyAPICommonUtilities;
import com.nishant.springboot.currencyutilities.model.Currency;
import com.nishant.springboot.currencyutilities.model.CurrencyConversionResponse;
import com.nishant.springboot.currencyutilities.model.Rates;
import com.nishant.springboot.currencyutilities.model.ResponseStatus;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPIErrorResponse;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPIInputValidationException;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPIServerValidationException;
import com.nishant.springboot.currencyutilities.model.errorhandling.CurrencyAPITechnicalException;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import static com.nishant.springboot.currencyutilities.configuration.common.constants.CurrencyAPICommonConstants.FLOATING_POINT_REGEX;

@RestController
@RequestMapping("/api/v1/currency/converter/")
public class CurrencyConverterController {

    CurrencyClientInterface currencyFeignClient;
    Logger logger = LoggerFactory.getLogger(this.getClass());

    public CurrencyConverterController(CurrencyClientInterface currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyConversionResponse getConversionRate(@RequestParam("Amount") String amount, @RequestParam("From") String from, @RequestParam("To") String to)
            throws CurrencyAPITechnicalException, CurrencyAPIInputValidationException, CurrencyAPIServerValidationException {


        validateAmount(amount);

        Rates apiRates = getRatesFromAPI();

        validateCurrency(from, apiRates);
        validateCurrency(to, apiRates);

        Map<String, Double> rates = apiRates.getRates();

        Double amountValue = Double.parseDouble(amount);
        Double fromRate = rates.get(from);
        Double toRate = rates.get(to);
        Date rateAsOf = apiRates.getDate();

        Double result = amountValue * toRate / fromRate;
        Double conversionRate = toRate / fromRate;

        ResponseStatus successStatus = new ResponseStatus("Success", null, null);
        CurrencyConversionResponse response = new CurrencyConversionResponse(from, to, rateAsOf, amountValue,
                conversionRate, result, successStatus);
        logger.info(response.toString());
        return response;
    }

    private Rates getRatesFromAPI() throws CurrencyAPITechnicalException, CurrencyAPIServerValidationException {
        Rates apiRates;
        String key = System.getenv("API_KEY");
        try {
            apiRates = currencyFeignClient.getLatestRates(key);
        } catch (Exception e) {
            throw new CurrencyAPITechnicalException(e);
        }
        if (apiRates == null) {
            throw new CurrencyAPITechnicalException("No Response from Currency API");
        }
        if (apiRates.getError() != null) {
            CurrencyAPIErrorResponse error = apiRates.getError();
            throw new CurrencyAPIServerValidationException(new ResponseStatus("Failed", error.getType(), error.getInfo()));
        }
        return apiRates;
    }

    private void validateCurrency(String currencySymbol, Rates rates) throws CurrencyAPIInputValidationException {
        Set<Currency> supportedCurrencies = CurrencyAPICommonUtilities.getSupportedCurrencies(rates);
        boolean matchFound = supportedCurrencies.stream().map(Currency::getCurrencySymbol).anyMatch(t -> t.equals(currencySymbol));
        if (!matchFound) {
            ResponseStatus errorResponse = new ResponseStatus("Failed", "Invalid Input", "Currency '" + currencySymbol + "' is not supported");
            throw new CurrencyAPIInputValidationException(errorResponse);

        }
    }

    private void validateAmount(String amount) throws CurrencyAPIInputValidationException {
        if (amount == null || "".equals(amount) || !Pattern.matches(FLOATING_POINT_REGEX, amount)) {
            ResponseStatus errorResponse = new ResponseStatus("Failed", "Invalid Input", "Amount '" + amount + "' not valid");
            throw new CurrencyAPIInputValidationException(errorResponse);
        }
    }
}

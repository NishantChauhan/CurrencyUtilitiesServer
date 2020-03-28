package com.nishant.springboot.currencyutilities.restcontrollers;

import com.nishant.springboot.currencyutilities.model.APIError;
import com.nishant.springboot.currencyutilities.model.CurrencyConversionResponse;
import com.nishant.springboot.currencyutilities.model.Rates;
import com.nishant.springboot.currencyutilities.model.ResponseStatus;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/currency/converter/")
public class CurrencyConverterController {

    CurrencyClientInterface currencyFeignClient;

    public CurrencyConverterController(CurrencyClientInterface currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyConversionResponse getConversionRate(@RequestParam("Amount") String amount, @RequestParam("From") String from, @RequestParam("To") String to) {
        String key = System.getenv("API_KEY");

        Double result = 0.0;
        Double amountValue = 0.0;
        Date rateAsOf = new Date();
        Double conversionRate = 0.0;

        Rates apiRates;

        ResponseStatus status = new ResponseStatus();
        CurrencyConversionResponse response;

        try {
            apiRates = currencyFeignClient.getLatestRates(key);
        } catch (Exception e) {
            status.setStatus("Failed");
            status.setErrorCode("Error while accessing Currency API");
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            status.setErrorDescription(sw.toString());
            response = new CurrencyConversionResponse(from, to, rateAsOf, amountValue, conversionRate, result, status);
            return response;
        }
        if (apiRates == null) {
            status.setStatus("Failed");
            status.setErrorCode("Error while accessing Currency API");
            status.setErrorDescription("No Response from Currency API");
            response = new CurrencyConversionResponse(from, to, rateAsOf, amountValue, conversionRate, result, status);
            return response;
        }
        APIError error = apiRates.getError();
        if (error != null) {
            status.setStatus("Failed");
            status.setErrorCode(error.getType());
            status.setErrorDescription(error.getInfo());
            response = new CurrencyConversionResponse(from, to, rateAsOf, amountValue, conversionRate, result, status);
            return response;
        }
        status.setStatus("Success");

        Map<String, Double> rates = apiRates.getRates();


        Double fromRate = rates.get(from);
        Double toRate = rates.get(to);
        rateAsOf = apiRates.getDate();

        try {
            amountValue = Double.parseDouble(amount);
        } catch (NumberFormatException nfe) {
            status.setStatus("Failed");
            status.setErrorCode("Invalid Input");
            status.setErrorDescription("Amount '" + amount + "' not valid");
            // TODO Throw proper HTTP Error Code
        }
        try {
            result = amountValue * toRate / fromRate;
            conversionRate = toRate / fromRate;
        } catch (Exception e) {

            result = 0.0;
            String errorCode = null;
            String errorDesc = null;

            if (toRate == null || toRate == 0.0) {
                errorCode = "Invalid Input";
                errorDesc = "Target Currency '" + to + "' is not supported";
            }

            if (fromRate == null || fromRate == 0.0) {
                errorCode = "Invalid Input";
                errorDesc = "Source Currency '" + from + "' is not supported";
            }

            status.setStatus("Failed");
            status.setErrorCode(errorCode);
            status.setErrorDescription(errorDesc);
        }
        response = new CurrencyConversionResponse(from, to, rateAsOf, amountValue, conversionRate, result, status);
        return response;
    }
}

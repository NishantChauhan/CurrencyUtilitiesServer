package com.nishant.springboot.currencyutilities.restcontrollers;

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
import java.util.Map;

@RestController
@RequestMapping("/api/currency/converter")
public class CurrencyConverterController {

    CurrencyClientInterface currencyFeignClient;

    public CurrencyConverterController(CurrencyClientInterface currencyFeignClient) {
        this.currencyFeignClient = currencyFeignClient;
    }

    @GetMapping(value = "/convert", produces = MediaType.APPLICATION_JSON_VALUE)
    public CurrencyConversionResponse getConversionRate(@RequestParam("Amount") String amount, @RequestParam("From") String from, @RequestParam("To") String to) {
        String key = System.getenv("API_KEY");
        Rates apiRates = currencyFeignClient.getLatestRates(key);

        Map<String, Double> rates = apiRates.getRates();

        Double result = 0.0;
        ResponseStatus status = new ResponseStatus();
        status.setStatus("Success");

        Double fromRate = rates.get(from);
        Double toRate = rates.get(to);
        Double amountValue = 0.0;

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
        } catch (Exception e) {

            result = 0.0;
            String errorCode = "Unknown error";
            String errorDesc = "Error while converting value";


            if (toRate == null || toRate == 0.0) {
                errorCode = "Invalid Input";
                errorDesc = "Target Currency '" + to + "' is not supported";
            }

            if (fromRate == null || fromRate == 0.0) {
                errorCode = "Invalid Input";
                errorDesc = "Source Currency '" + from + "' is not supported";
            }

            if (errorDesc == null) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                errorDesc = "Error while converting value\n\n" + sw.toString();
            }

            status.setStatus("Failed");
            status.setErrorCode(errorCode);
            status.setErrorDescription(errorDesc);
        }
        CurrencyConversionResponse response = new CurrencyConversionResponse(from, to, amountValue, result, status);
        return response;
    }
}

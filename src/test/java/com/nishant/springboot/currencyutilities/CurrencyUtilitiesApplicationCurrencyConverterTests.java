package com.nishant.springboot.currencyutilities;

import com.nishant.springboot.currencyutilities.local.commonutils.RateUtils;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.net.ConnectException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyUtilitiesApplicationCurrencyConverterTests {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CurrencyClientInterface clientInterface;

    @Test
    public void shouldConvert1000CADtoINR() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        this.mockMvc.perform(get("/api/v1/currency/converter/convert")
                .param("Amount", "1000")
                .param("From", "CAD")
                .param("To", "INR")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.from").value("CAD"))
                .andExpect(jsonPath("$.to").value("INR"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.rateAsOf").isNotEmpty())
                .andExpect(jsonPath("$.conversionRate").value(53.49008750820958))
                .andExpect(jsonPath("$.result").value(53490.08750820958))
                .andExpect(jsonPath("$.responseStatus.status").value("Success"))
        ;
    }

    @Test
    public void shouldReturnErrorForCADAtoINRConversion() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        //noinspection SpellCheckingInspection
        this.mockMvc
                .perform(get("/api/v1/currency/converter/convert")
                        .param("Amount", "1000")
                        .param("From", "CADA")
                        .param("To", "INR")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("Failed"))
                .andExpect(jsonPath("$.errorCode").value("Invalid Input"))
                .andExpect(jsonPath("$.errorDescription").value("Currency 'CADA' is not supported"));
    }

    @Test
    public void shouldReturnErrorForInvalidAccessKey() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getAccessKeyError());
        this.mockMvc
                .perform(get("/api/v1/currency/converter/convert")
                        .param("Amount", "1000")
                        .param("From", "CAD")
                        .param("To", "INR")
                )
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("Failed"))
                .andExpect(jsonPath("$.errorCode").value("Validation Error occurred while invoking Currency Provider API"))
                .andExpect(jsonPath("$.errorDescription").value("invalid_access_key"));
    }

    @Test
    public void shouldReturnErrorWhenProvideAPIisUnavailable() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenThrow(new RuntimeException(new ConnectException()));
        this.mockMvc
                .perform(get("/api/v1/currency/converter/convert")
                        .param("Amount", "1000")
                        .param("From", "CAD")
                        .param("To", "INR")
                )
                .andExpect(status().is5xxServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.status").value("Failed"))
                .andExpect(jsonPath("$.errorCode").value("Error while accessing Currency API"))
                .andExpect(jsonPath("$.errorDescription").value("Unable to connect to Currency Provider API"));
    }
}

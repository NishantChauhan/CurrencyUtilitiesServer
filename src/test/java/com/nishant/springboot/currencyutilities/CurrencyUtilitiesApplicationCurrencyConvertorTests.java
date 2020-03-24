package com.nishant.springboot.currencyutilities;

import com.nishant.springboot.currencyutilities.commonutils.RateUtils;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyUtilitiesApplicationCurrencyConvertorTests {
    @Autowired
    private MockMvc mockMvc;


    @MockBean
    private CurrencyClientInterface clientInterface;

    @Test
    public void shouldConvert1000CADtoINR() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        this.mockMvc.perform(get("/api/currency/converter/convert")
                .param("Amount", "1000")
                .param("From", "CAD")
                .param("To", "INR")
        ).andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.from").value("CAD"))
                .andExpect(jsonPath("$.to").value("INR"))
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.result").value(53490.08750820958))
                .andExpect(jsonPath("$.responseStatus.status").value("Success"))
        ;
    }

    @Test
    public void shouldReturnErrorForCADAtoINRConversion() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        //noinspection SpellCheckingInspection
        this.mockMvc
                .perform(get("/api/currency/converter/convert")
                        .param("Amount", "1000")
                        .param("From", "CADA")
                        .param("To", "INR")
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.responseStatus.status").value("Failed"))
                .andExpect(jsonPath("$.responseStatus.errorCode").value("Invalid Input"))
                .andExpect(jsonPath("$.responseStatus.errorDescription").value("Source Currency 'CADA' is not supported"));
    }
}

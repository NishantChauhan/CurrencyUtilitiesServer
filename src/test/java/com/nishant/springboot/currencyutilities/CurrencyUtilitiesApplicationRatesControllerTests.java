package com.nishant.springboot.currencyutilities;

import com.nishant.springboot.currencyutilities.local.commonutils.RateUtils;
import com.nishant.springboot.currencyutilities.restcontrollers.feignclients.CurrencyClientInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyUtilitiesApplicationRatesControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyClientInterface clientInterface;

    @Test
    public void shouldReturnLatestRates() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        this.mockMvc.perform(get("/api/v1/currency/rates/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.base").isString())
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.rates.CAD").isNumber())
                .andExpect(jsonPath("$.rates.INR").isNumber());
    }

    @Test
    public void shouldReturnAllSupportedCurrencies() throws Exception {
        Mockito.when(clientInterface.getLatestRates(System.getenv("API_KEY"))).thenReturn(RateUtils.getLatestRates());
        this.mockMvc.perform(get("/api/v1/currency/rates/supportedCurrencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[3].currencySymbol").value("CAD"))
                .andExpect(jsonPath("$.[14].currencySymbol").value("INR"))
                .andExpect(jsonPath("$.[30].currencySymbol").value("USD"));
    }
}

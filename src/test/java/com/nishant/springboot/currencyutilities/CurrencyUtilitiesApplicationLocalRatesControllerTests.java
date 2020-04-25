package com.nishant.springboot.currencyutilities;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CurrencyUtilitiesApplicationLocalRatesControllerTests {
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void shouldReturnLocalLatestRates() throws Exception {
        this.mockMvc.perform(get("/latest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.timestamp").isNumber())
                .andExpect(jsonPath("$.base").isString())
                .andExpect(jsonPath("$.date").isNotEmpty())
                .andExpect(jsonPath("$.rates.CAD").isNumber())
                .andExpect(jsonPath("$.rates.INR").isNumber());
    }
}

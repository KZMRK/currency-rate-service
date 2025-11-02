package com.kazmiruk.currencyrateservice.controller;

import com.kazmiruk.currencyrateservice.model.dto.GroupedCurrencyRatesDto;
import com.kazmiruk.currencyrateservice.service.CurrencyRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildCryptoCurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatCurrencyRateDtos;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CurrencyRateController.class)
public class CurrencyRateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CurrencyRateService currencyRateService;

    @Test
    void getCurrencyRates_shouldReturn_shouldReturnExpectedResponse() throws Exception {
        // given
        var response = GroupedCurrencyRatesDto.builder()
                .crypto(buildFiatCurrencyRateDtos())
                .fiat(buildCryptoCurrencyRateDtos())
                .build();

        // when
        when(currencyRateService.getCurrencyRates())
                .thenReturn(CompletableFuture.completedFuture(response));

        // then
        var mvcResult = mvc.perform(get("/currency-rates"))
                .andExpect(request().asyncStarted())
                .andReturn();
        mvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(content().json("""
                {
                     "fiat": [
                         {
                             "currency": "BTC",
                             "rate": 42432.47485905939
                         },
                         {
                             "currency": "ETH",
                             "rate": 54018.85294838679
                         },
                         {
                             "currency": "LTC",
                             "rate": 27754.27146053405
                         }
                     ],
                     "crypto": [
                         {
                             "currency": "USD",
                             "rate": 50.72816587495561
                         },
                         {
                             "currency": "EUR",
                             "rate": 3.3646514154394644
                         },
                         {
                             "currency": "GBP",
                             "rate": 41.68449022543177
                         }
                     ]
                }
                """));
    }
}

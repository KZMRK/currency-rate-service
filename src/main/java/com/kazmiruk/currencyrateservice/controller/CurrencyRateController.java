package com.kazmiruk.currencyrateservice.controller;

import com.kazmiruk.currencyrateservice.model.dto.GroupedCurrencyRatesDto;
import com.kazmiruk.currencyrateservice.service.CurrencyRateService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequiredArgsConstructor
public class CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    @GetMapping(value = "/currency-rates", produces = APPLICATION_JSON_VALUE)
    public CompletableFuture<GroupedCurrencyRatesDto> getCurrencyRates() {
        return currencyRateService.getCurrencyRates();
    }
}

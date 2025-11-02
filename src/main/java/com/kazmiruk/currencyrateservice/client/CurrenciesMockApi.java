package com.kazmiruk.currencyrateservice.client;

import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.CryptoCurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.FiatCurrencyRateDto;
import feign.RequestLine;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CurrenciesMockApi {

    @RequestLine("GET /fiat-currency-rates")
    CompletableFuture<List<FiatCurrencyRateDto>> getFiatCurrencyRates();

    @RequestLine("GET /crypto-currency-rates")
    CompletableFuture<List<CryptoCurrencyRateDto>> getCryptoCurrencyRates();
}

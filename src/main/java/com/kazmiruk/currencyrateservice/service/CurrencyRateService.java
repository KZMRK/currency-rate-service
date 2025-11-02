package com.kazmiruk.currencyrateservice.service;

import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.GroupedCurrencyRatesDto;
import com.kazmiruk.currencyrateservice.service.strategy.CurrencyRateStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.CRYPTO;
import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.FIAT;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrencyRateService {

    private final CurrencyRateStrategy currencyRateStrategy;

    public CompletableFuture<GroupedCurrencyRatesDto> getCurrencyRates() {
        log.info("[getCurrencyRates] invoked");
        var fiatFuture = currencyRateStrategy.getCurrencyRatesFor(FIAT);
        var cryptoFuture = currencyRateStrategy.getCurrencyRatesFor(CRYPTO);
        return fiatFuture.thenCombine(cryptoFuture, this::buildResponse);
    }

    private GroupedCurrencyRatesDto buildResponse(List<CurrencyRateDto> fiatDtos, List<CurrencyRateDto> cryptoDtos) {
        return GroupedCurrencyRatesDto.builder()
                .fiat(fiatDtos)
                .crypto(cryptoDtos)
                .build();
    }
}

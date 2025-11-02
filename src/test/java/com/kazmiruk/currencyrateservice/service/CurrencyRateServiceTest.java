package com.kazmiruk.currencyrateservice.service;

import com.kazmiruk.currencyrateservice.model.dto.GroupedCurrencyRatesDto;
import com.kazmiruk.currencyrateservice.service.strategy.CurrencyRateStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.CRYPTO;
import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.FIAT;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildCryptoCurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatCurrencyRateDtos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    @InjectMocks
    private CurrencyRateService currencyRateService;

    @Mock
    private CurrencyRateStrategy currencyRateStrategy;

    @Test
    void getCurrencyRates_shouldReturnAll() {
        // given
        var fiatCurrencyRates = buildFiatCurrencyRateDtos();
        var cryptoCurrencyRates = buildCryptoCurrencyRateDtos();
        var expectedCurrencyRates = GroupedCurrencyRatesDto.builder()
                .fiat(fiatCurrencyRates)
                .crypto(cryptoCurrencyRates)
                .build();

        // when
        when(currencyRateStrategy.getCurrencyRatesFor(FIAT))
                .thenReturn(CompletableFuture.completedFuture(fiatCurrencyRates));
        when(currencyRateStrategy.getCurrencyRatesFor(CRYPTO))
                .thenReturn(CompletableFuture.completedFuture(cryptoCurrencyRates));


        // then
        var actualCurrencyRates = currencyRateService.getCurrencyRates();
        assertThat(actualCurrencyRates.join())
                .usingRecursiveComparison()
                .isEqualTo(expectedCurrencyRates);
    }

}
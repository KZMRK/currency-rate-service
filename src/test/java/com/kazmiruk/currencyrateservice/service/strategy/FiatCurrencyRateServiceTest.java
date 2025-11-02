package com.kazmiruk.currencyrateservice.service.strategy;

import com.kazmiruk.currencyrateservice.client.CurrenciesMockApi;
import com.kazmiruk.currencyrateservice.mapper.CurrencyRateMapper;
import com.kazmiruk.currencyrateservice.model.exception.CurrenciesMockException;
import com.kazmiruk.currencyrateservice.repository.CurrencyRateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.FIAT;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatAPICurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatCurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatCurrencyRateEntities;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FiatCurrencyRateServiceTest {

    @InjectMocks
    private FiatCurrencyRateService fiatCurrencyRateService;

    @Mock
    protected CurrencyRateMapper currencyRateMapper;

    @Mock
    protected CurrenciesMockApi currenciesMockApi;

    @Mock
    private CurrencyRateRepository currencyRateRepository;

    @Test
    void getCurrencyRates_whenAPIReturnSuccessResponse_shouldReturnFiatCurrencyRates() {
        // given
        var fiatCurrencyRates = buildFiatAPICurrencyRateDtos();
        var currencyRates = buildFiatCurrencyRateDtos();
        var expectedCurrencyRates = buildFiatCurrencyRateDtos();

        // when
        when(currenciesMockApi.getFiatCurrencyRates())
                .thenReturn(CompletableFuture.completedFuture(fiatCurrencyRates));
        when(currencyRateMapper.toDto(anyList()))
                .thenReturn(currencyRates);

        // then
        var actualCurrencyRates = fiatCurrencyRateService.getCurrencyRates().join();
        assertThat(actualCurrencyRates)
                .usingRecursiveComparison()
                .isEqualTo(expectedCurrencyRates);
    }

    @Test
    void getCurrencyRates_whenAPIReturnError_shouldReturnFiatCurrencyRatesFromTable() {
        // given
        var fiatCurrencyRates = buildFiatCurrencyRateEntities();
        var currencyRates = buildFiatCurrencyRateDtos();
        var expectedCurrencyRates = buildFiatCurrencyRateDtos();


        // when
        when(currenciesMockApi.getFiatCurrencyRates())
                .thenReturn(CompletableFuture.failedFuture(new CurrenciesMockException()));
        when(currencyRateRepository.findLatestByType(FIAT))
                .thenReturn(fiatCurrencyRates);
        when(currencyRateMapper.toDto(anyList()))
                .thenReturn(currencyRates);

        // then
        var actualCurrencyRates = fiatCurrencyRateService.getCurrencyRates().join();
        assertThat(actualCurrencyRates)
                .usingRecursiveComparison()
                .isEqualTo(expectedCurrencyRates);
    }

}
package com.kazmiruk.currencyrateservice.controller;

import com.kazmiruk.currencyrateservice.client.CurrenciesMockApi;
import com.kazmiruk.currencyrateservice.model.dto.GroupedCurrencyRatesDto;
import com.kazmiruk.currencyrateservice.model.exception.CurrenciesMockException;
import com.kazmiruk.currencyrateservice.repository.CurrencyRateRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildCryptoCurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildCryptoAPICurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatAPICurrencyRateDtos;
import static com.kazmiruk.currencyrateservice.utils.CurrencyRateUtils.buildFiatCurrencyRateDtos;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CurrencyRateControllerIntegrationTest {

    @MockitoBean
    private CurrenciesMockApi currenciesMockApi;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:14.19-alpine3.21");

    @BeforeAll
    static void beforeAll() {
        postgres.start();
    }

    @AfterAll
    static void afterAll() {
        postgres.stop();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Test
    @Order(1)
    void getCurrencyRates_whenBothAPIsReturnErrorAndTableIsEmpty_shouldReturnEmptyLists() {
        // given

        // when
        when(currenciesMockApi.getFiatCurrencyRates())
                .thenReturn(CompletableFuture.failedFuture(new CurrenciesMockException()));
        when(currenciesMockApi.getCryptoCurrencyRates())
                .thenReturn(CompletableFuture.failedFuture(new CurrenciesMockException()));

        // then
        var response = testRestTemplate.getForEntity("/currency-rates", GroupedCurrencyRatesDto.class);
        var actualCurrencyRates = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(actualCurrencyRates).isNotNull();
        assertThat(actualCurrencyRates.getFiat()).isEmpty();
        assertThat(actualCurrencyRates.getCrypto()).isEmpty();
    }

    @Test
    @Order(2)
    void getCurrencyRates_whenBothAPIsReturnRates_should() {
        // given
        var fiatCurrencyRates = buildFiatAPICurrencyRateDtos();
        var cryptoCurrencyRates = buildCryptoAPICurrencyRateDtos();
        var expectedCurrencyRates = GroupedCurrencyRatesDto.builder()
                .crypto(buildCryptoCurrencyRateDtos())
                .fiat(buildFiatCurrencyRateDtos())
                .build();

        // when
        when(currenciesMockApi.getFiatCurrencyRates())
                .thenReturn(CompletableFuture.completedFuture(fiatCurrencyRates));
        when(currenciesMockApi.getCryptoCurrencyRates())
                .thenReturn(CompletableFuture.completedFuture(cryptoCurrencyRates));

        // then
        var response = testRestTemplate.getForEntity("/currency-rates", GroupedCurrencyRatesDto.class);
        var actualCurrencyRates = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(actualCurrencyRates).isNotNull();
        assertThat(actualCurrencyRates)
                .usingRecursiveComparison()
                .isEqualTo(expectedCurrencyRates);
        assertThat(currencyRateRepository.count()).isEqualTo(6);
    }

    @Test
    @Order(3)
    void getCurrencyRates_whenBothAPIsReturnErrorAndTableHasRecords_shouldReturnDataFromTable() {
        // given
        var expectedCurrencyRates = GroupedCurrencyRatesDto.builder()
                .crypto(buildCryptoCurrencyRateDtos())
                .fiat(buildFiatCurrencyRateDtos())
                .build();

        // when
        when(currenciesMockApi.getFiatCurrencyRates())
                .thenReturn(CompletableFuture.failedFuture(new CurrenciesMockException()));
        when(currenciesMockApi.getCryptoCurrencyRates())
                .thenReturn(CompletableFuture.failedFuture(new CurrenciesMockException()));

        // then
        var response = testRestTemplate.getForEntity("/currency-rates", GroupedCurrencyRatesDto.class);
        var actualCurrencyRates = response.getBody();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertThat(actualCurrencyRates).isNotNull();
        assertThat(actualCurrencyRates.getCrypto())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedCurrencyRates.getCrypto());

        assertThat(actualCurrencyRates.getFiat())
                .usingRecursiveFieldByFieldElementComparator()
                .containsExactlyInAnyOrderElementsOf(expectedCurrencyRates.getFiat());
    }
}
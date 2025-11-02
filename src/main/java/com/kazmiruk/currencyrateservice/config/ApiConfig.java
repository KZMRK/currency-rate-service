package com.kazmiruk.currencyrateservice.config;

import com.kazmiruk.currencyrateservice.client.CurrenciesMockApi;
import com.kazmiruk.currencyrateservice.model.exception.CurrenciesMockException;
import feign.AsyncFeign;
import feign.RequestInterceptor;
import feign.gson.GsonDecoder;
import feign.okhttp.OkHttpClient;
import feign.slf4j.Slf4jLogger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;

import static feign.Logger.Level.BASIC;

@Slf4j
@Configuration
public class ApiConfig {

    private final String API_KEY_HEADER = "X-API-KEY";

    @Bean
    public CurrenciesMockApi currenciesMockApi(@Value("${currency-mock.domain}") String domain,
                                               @Value("${currency-mock.api-key}") String apiKey) {
        RequestInterceptor requestInterceptor = request -> request.header(API_KEY_HEADER, apiKey);
        return AsyncFeign.builder()
                .client(new OkHttpClient())
                .decoder(new GsonDecoder())
                .requestInterceptor(requestInterceptor)
                .logger(new Slf4jLogger(CurrenciesMockApi.class))
                .logLevel(BASIC)
                .errorDecoder((s, response) -> {
                    log.warn("[decode] Currencies Mocks API responded with an error. Status=[{}]", HttpStatus.valueOf(response.status()));
                    return new CurrenciesMockException();
                })
                .target(CurrenciesMockApi.class, domain);
    }
}

package com.kazmiruk.currencyrateservice.service.strategy;

import com.kazmiruk.currencyrateservice.client.CurrenciesMockApi;
import com.kazmiruk.currencyrateservice.mapper.CurrencyRateMapper;
import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.entity.CurrencyRate;
import com.kazmiruk.currencyrateservice.model.exception.CurrenciesMockException;
import com.kazmiruk.currencyrateservice.model.exception.CurrencyRateServiceException;
import com.kazmiruk.currencyrateservice.model.type.CurrencyRateType;
import com.kazmiruk.currencyrateservice.repository.CurrencyRateRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class AbstractCurrencyRateService {

    @Autowired
    protected CurrencyRateMapper currencyRateMapper;
    @Autowired
    protected CurrenciesMockApi currenciesMockApi;
    @Autowired
    private CurrencyRateRepository currencyRateRepository;

    abstract CurrencyRateType getCurrencyRateType();

    abstract CompletableFuture<List<CurrencyRateDto>> getCurrencyRates();

    protected <D> CompletableFuture<List<CurrencyRateDto>> getCurrencyRates(
            Supplier<CompletableFuture<List<D>>> supplier,
            Function<D, CurrencyRate> mapper) {
        return supplier.get()
                .thenApply(dtos -> createAll(dtos, mapper))
                .exceptionally(this::handleException)
                .thenApply(currencyRateMapper::toDto);
    }

    private <D> List<CurrencyRate> createAll(List<D> dtos, Function<D, CurrencyRate> mapper) {
        var entities = dtos.stream().map(mapper).toList();
        return currencyRateRepository.saveAll(entities);
    }

    private List<CurrencyRate> handleException(Throwable e) {
        if (e.getCause() instanceof CurrenciesMockException) {
            return currencyRateRepository.findLatestByType(getCurrencyRateType());
        }
        throw new CurrencyRateServiceException("internal exception");
    }
}

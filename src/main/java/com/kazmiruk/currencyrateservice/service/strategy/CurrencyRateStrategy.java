package com.kazmiruk.currencyrateservice.service.strategy;

import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.exception.CurrencyRateServiceException;
import com.kazmiruk.currencyrateservice.model.type.CurrencyRateType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CurrencyRateStrategy {

    private Map<CurrencyRateType, AbstractCurrencyRateService> delegates;

    @Autowired
    void registerDelegates(List<AbstractCurrencyRateService> services) {
        delegates = services.stream()
                .collect(Collectors.toMap(
                        AbstractCurrencyRateService::getCurrencyRateType,
                        Function.identity())
                );
    }

    public CompletableFuture<List<CurrencyRateDto>> getCurrencyRatesFor(CurrencyRateType type) {
        var service = getServiceFor(type);
        return service.getCurrencyRates();
    }

    private AbstractCurrencyRateService getServiceFor(CurrencyRateType type) {
        return Optional.ofNullable(delegates.get(type))
                .orElseThrow(() -> new CurrencyRateServiceException("service for type=[%s] not found".formatted(type)));
    }
}

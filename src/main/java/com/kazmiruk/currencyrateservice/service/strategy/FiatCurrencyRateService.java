package com.kazmiruk.currencyrateservice.service.strategy;

import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.type.CurrencyRateType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.*;

@Service
class FiatCurrencyRateService extends AbstractCurrencyRateService {

    @Override
    CurrencyRateType getCurrencyRateType() {
        return FIAT;
    }

    @Override
    CompletableFuture<List<CurrencyRateDto>> getCurrencyRates() {
        return getCurrencyRates(currenciesMockApi::getFiatCurrencyRates, currencyRateMapper::toEntity);
    }
}

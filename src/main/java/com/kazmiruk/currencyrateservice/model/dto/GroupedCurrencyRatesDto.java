package com.kazmiruk.currencyrateservice.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class GroupedCurrencyRatesDto {

    private List<CurrencyRateDto> fiat;

    private List<CurrencyRateDto> crypto;
}

package com.kazmiruk.currencyrateservice.model.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CurrencyRateDto {

    private String currency;

    private BigDecimal rate;
}

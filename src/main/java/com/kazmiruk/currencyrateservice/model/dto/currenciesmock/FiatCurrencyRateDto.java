package com.kazmiruk.currencyrateservice.model.dto.currenciesmock;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FiatCurrencyRateDto {

    private String currency;

    private BigDecimal rate;
}

package com.kazmiruk.currencyrateservice.model.dto.currenciesmock;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CryptoCurrencyRateDto {

    private String name;

    private BigDecimal value;
}

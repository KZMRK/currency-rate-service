package com.kazmiruk.currencyrateservice.utils;

import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.CryptoCurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.FiatCurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.entity.CurrencyRate;
import com.kazmiruk.currencyrateservice.model.type.CurrencyRateType;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.CRYPTO;
import static com.kazmiruk.currencyrateservice.model.type.CurrencyRateType.FIAT;

@UtilityClass
public class CurrencyRateUtils {

    public static List<CurrencyRateDto> buildFiatCurrencyRateDtos() {
        return List.of(
                buildCurrencyRateDto("USD", BigDecimal.valueOf(50.72816587495561)),
                buildCurrencyRateDto("EUR", BigDecimal.valueOf(3.3646514154394644)),
                buildCurrencyRateDto("GBP", BigDecimal.valueOf(41.68449022543177))
        );
    }

    public static List<CurrencyRateDto> buildCryptoCurrencyRateDtos() {
        return List.of(
                buildCurrencyRateDto("BTC", BigDecimal.valueOf(42432.47485905939)),
                buildCurrencyRateDto("ETH", BigDecimal.valueOf(54018.85294838679)),
                buildCurrencyRateDto("LTC", BigDecimal.valueOf(27754.27146053405))
        );
    }

    public static List<FiatCurrencyRateDto> buildFiatAPICurrencyRateDtos() {
        return List.of(
                buildFiatAPICurrencyRateDto("USD", BigDecimal.valueOf(50.72816587495561)),
                buildFiatAPICurrencyRateDto("EUR", BigDecimal.valueOf(3.3646514154394644)),
                buildFiatAPICurrencyRateDto("GBP", BigDecimal.valueOf(41.68449022543177))
        );
    }

    public static List<CryptoCurrencyRateDto> buildCryptoAPICurrencyRateDtos() {
        return List.of(
                buildCryptoAPICurrencyRateDto("BTC", BigDecimal.valueOf(42432.47485905939)),
                buildCryptoAPICurrencyRateDto("ETH", BigDecimal.valueOf(54018.85294838679)),
                buildCryptoAPICurrencyRateDto("LTC", BigDecimal.valueOf(27754.27146053405))
        );
    }

    public static List<CurrencyRate> buildCryptoCurrencyRateEntities() {
        return List.of(
                buildCurrencyRate(CRYPTO, "BTC", BigDecimal.valueOf(42432.47485905939)),
                buildCurrencyRate(CRYPTO, "ETH", BigDecimal.valueOf(54018.85294838679)),
                buildCurrencyRate(CRYPTO, "LTC", BigDecimal.valueOf(27754.27146053405))
        );
    }

    public static List<CurrencyRate> buildFiatCurrencyRateEntities() {
        return List.of(
                buildCurrencyRate(FIAT, "USD", BigDecimal.valueOf(50.72816587495561)),
                buildCurrencyRate(FIAT, "EUR", BigDecimal.valueOf(3.3646514154394644)),
                buildCurrencyRate(FIAT, "GBP", BigDecimal.valueOf(41.68449022543177))
        );
    }

    public static FiatCurrencyRateDto buildFiatAPICurrencyRateDto(String currency, BigDecimal rate) {
        var dto = new FiatCurrencyRateDto();
        dto.setCurrency(currency);
        dto.setRate(rate);
        return dto;
    }

    public static CryptoCurrencyRateDto buildCryptoAPICurrencyRateDto(String name, BigDecimal value) {
        var dto = new CryptoCurrencyRateDto();
        dto.setName(name);
        dto.setValue(value);
        return dto;
    }

    private static CurrencyRateDto buildCurrencyRateDto(String currency, BigDecimal rate) {
        var dto = new CurrencyRateDto();
        dto.setCurrency(currency);
        dto.setRate(rate);
        return dto;
    }

    private static CurrencyRate buildCurrencyRate(CurrencyRateType type, String currency, BigDecimal rate) {
        var entity = new CurrencyRate();
        entity.setId(UUID.randomUUID());
        entity.setType(type);
        entity.setCurrency(currency);
        entity.setRate(rate);
        entity.setCreatedAt(LocalDateTime.now());
        return entity;
    }
}

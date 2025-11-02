package com.kazmiruk.currencyrateservice.mapper;

import com.kazmiruk.currencyrateservice.model.dto.CurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.CryptoCurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.dto.currenciesmock.FiatCurrencyRateDto;
import com.kazmiruk.currencyrateservice.model.entity.CurrencyRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, unmappedTargetPolicy = IGNORE)
public interface CurrencyRateMapper {

    @Mapping(target = "type", constant = "FIAT")
    CurrencyRate toEntity(FiatCurrencyRateDto dto);

    @Mapping(target = "currency", source = "name")
    @Mapping(target = "rate", source = "value")
    @Mapping(target = "type", constant = "CRYPTO")
    CurrencyRate toEntity(CryptoCurrencyRateDto dto);

    List<CurrencyRateDto> toDto(List<CurrencyRate> entities);

    @Mapping(target = "rate", expression = "java(entity.getRate().stripTrailingZeros())")
    CurrencyRateDto toDto(CurrencyRate entity);
}

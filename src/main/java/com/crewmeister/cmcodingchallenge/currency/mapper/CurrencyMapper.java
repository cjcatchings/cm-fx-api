package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyDto;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    @Mappings({
            @Mapping(source="currencyCode", target="code"),
            @Mapping(source="currencyName", target="name")
    })
    CurrencyDto currencyToCurrencyDto(Currency currency);
}

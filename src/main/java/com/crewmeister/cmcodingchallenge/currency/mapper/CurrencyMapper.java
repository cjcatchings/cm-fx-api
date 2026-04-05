package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyDto;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

/**
 * Transforms `Currency` entities into serializable `CurrencyDto` objects
 */
@Mapper
public interface CurrencyMapper {

    CurrencyMapper INSTANCE = Mappers.getMapper(CurrencyMapper.class);

    /**
     * Converts a `Currency` entity into a `CurrencyDto` object that can be serialized into JSON
     * @param currency The `Currency` entity result to convert to DTO
     * @return the DTO representation of the `Currency` entity
     */
    @Mappings({
            @Mapping(source="currencyCode", target="code"),
            @Mapping(source="currencyName", target="name")
    })
    CurrencyDto currencyToCurrencyDto(Currency currency);
}

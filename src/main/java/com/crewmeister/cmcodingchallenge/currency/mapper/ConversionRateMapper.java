package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateDto;
import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Transforms `ConversionRate` entities into serializable `ConversionRateDto` objects
 */
@Mapper
public interface ConversionRateMapper {

    ConversionRateMapper INSTANCE = Mappers.getMapper(ConversionRateMapper.class);

    /**
     * Converts a `ConversionRate` entity into a `ConversionRateDto` object that can be serialized into JSON
     * @param conversionRate The `ConversionRate` entity result to convert to DTO
     * @return the DTO representation of the `ConversionRate` entity
     */
    @Mapping( source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    ConversionRateDto conversionRateToConversionRateDto(ConversionRate conversionRate);

}

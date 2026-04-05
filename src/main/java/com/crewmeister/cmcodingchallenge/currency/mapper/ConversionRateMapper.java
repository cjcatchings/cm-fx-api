package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateDto;
import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ConversionRateMapper {

    ConversionRateMapper INSTANCE = Mappers.getMapper(ConversionRateMapper.class);

    @Mapping( source = "date", target = "date", dateFormat = "yyyy-MM-dd")
    ConversionRateDto conversionRateToConversionRateDto(ConversionRate conversionRate);

}

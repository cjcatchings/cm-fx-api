package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionDto;

public class ConversionMapper {

    public static ConversionDto conversionToDto(Double rate, Double amountInEuros) {
        return new ConversionDto(rate, amountInEuros);
    }

}

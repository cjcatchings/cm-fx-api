package com.crewmeister.cmcodingchallenge.currency.mapper;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionDto;

/**
 * Transforms Conversion (into Euros) entities into serializable DTO objects
 */
public class ConversionMapper {

    /**
     * Convert data from a conversion into Euros (rate and amountInEuros) into a DTO object
     * @param rate The rate used to convert a given currency amount into Euros
     * @param amountInEuros The amount in Euros the currency was converted into
     * @return A conversion DTO representation that can be serialized into JSON
     */
    public static ConversionDto conversionToDto(Double rate, Double amountInEuros) {
        return new ConversionDto(rate, amountInEuros);
    }

}

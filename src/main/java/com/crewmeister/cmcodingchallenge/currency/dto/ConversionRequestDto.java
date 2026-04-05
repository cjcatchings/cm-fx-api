package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a conversion rate request to convert a given currency to Euros
 *  - sourceAmount - the amount (in a source currency) to convert to Euros
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRequestDto {
    private Double sourceAmount;
}

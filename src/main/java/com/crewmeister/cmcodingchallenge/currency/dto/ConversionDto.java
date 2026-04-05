package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a conversion from a given currency to Euros
 *  - rateUsed - The conversion rate (divided by) used to convert the given currency to Euros
 *  - amountInEuros - The amount of Euros that the given currency was converted into
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionDto {
    private Double rateUsed;
    private Double amountInEuros;
}

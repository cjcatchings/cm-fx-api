package com.crewmeister.cmcodingchallenge.currency.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a conversion rate of a given currency from Euros
 *  - date - The date on which you could convert from Euros to the given currency for the given rate
 *  - rate - The rate that one Euro would convert to for the given currency
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ConversionRateDto {

    private String date;

    private Double rate;

}

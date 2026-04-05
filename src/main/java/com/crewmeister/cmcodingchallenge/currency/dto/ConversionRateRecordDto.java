package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a conversion rate record retrieved from a CSV file when initially
 * loading conversion rates
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRateRecordDto {
    private String currencyCode;
    private String date;
    private String rate;
    private String metadata;
}

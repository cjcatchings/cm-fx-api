package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConversionRateRecordDto {
    private String currencyCode;
    private String date;
    private String rate;
    private String metadata;
}

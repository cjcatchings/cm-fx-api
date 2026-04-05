package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a currency record retrieved from a CSV file when initially
 * loading currencies
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRecordDto {
    private String code;
    private String name;
}

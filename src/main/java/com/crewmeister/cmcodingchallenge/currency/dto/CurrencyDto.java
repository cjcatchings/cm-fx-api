package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * A DTO representation of a currency available in the system
 *  - code - The 3-letter code for the currency
 *  - name - A descriptive name for the currency
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private String code;
    private String name;
}

package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrencyDto {
    private String code;
    private String name;
}

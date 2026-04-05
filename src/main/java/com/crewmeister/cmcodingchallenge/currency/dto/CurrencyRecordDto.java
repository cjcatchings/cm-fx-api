package com.crewmeister.cmcodingchallenge.currency.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRecordDto {
    private String code;
    private String name;
}

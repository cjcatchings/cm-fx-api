package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConversionRateTests {

    @Autowired
    private ConversionRateService conversionRateService;

    @Test
    void testCurrencyUsdHas6978ConversionRates() {
        // Given
        String currencyCodeUsd = "USD";

        // When
        List<ConversionRate> usdConversionRates = conversionRateService.getByCurrencyCode(currencyCodeUsd);

        // Then
        assertEquals(6978, usdConversionRates.size());
    }

    @Test
    void testCurrencyUsdOn20260330HasRateOf1p1484() throws ParseException, ConversionRateNotFoundException {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "2026-03-30";
        Double expectedRate = 1.1484;

        // When
        ConversionRate usdConversionRate = conversionRateService.getByCurrencyCodeAndDate(currencyCodeUsd, givenDate);

        // Then
        assertEquals(expectedRate, usdConversionRate.getRate());
    }
}

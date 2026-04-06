package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ConversionRateWithMaxResultsTests {

    @Autowired
    private ConversionRateService conversionRateService;

    @BeforeEach
    void setMaxResultsBefore() {
        ReflectionTestUtils.setField(conversionRateService, "maxResultsForGetRates", 30);
    }

    @AfterEach
    void setMaxResultsAfter() {
        ReflectionTestUtils.setField(conversionRateService, "maxResultsForGetRates", null);
    }

    @Test
    void testCurrencyUsdWithMaxResultsHas30ConversionRates() {
        // Given
        String currencyCodeUsd = "USD";
        Integer expectedConversionRates = 30;

        // When
        List<ConversionRate> usdConversionRates = conversionRateService.getByCurrencyCode(currencyCodeUsd, null, null);

        // Then
        assertEquals(expectedConversionRates, usdConversionRates.size());
    }

    @Test
    void testCurrencyUsdWithMaxResults30From20200101HasLatestRate1p1052(){
        // Given
        String currencyCodeUsd = "USD";
        Double expectedConversionRate = 1.1052;
        // When
        List<ConversionRate> usdConversionRates = conversionRateService.getByCurrencyCode(
                currencyCodeUsd,
                "2020-01-01",
                null
        );

        // Then
        assertEquals(expectedConversionRate, usdConversionRates.getFirst().getRate());
    }

    @Test
    void testCurrencyUsdWithMaxResults30To20200101HasLatestRate1p1234(){
        // Given
        String currencyCodeUsd = "USD";
        Double expectedConversionRate = 1.1234;
        // When
        List<ConversionRate> usdConversionRates = conversionRateService.getByCurrencyCode(
                currencyCodeUsd,
                null,
                "2020-01-01"
        );

        // Then
        assertEquals(expectedConversionRate, usdConversionRates.getFirst().getRate());
    }
}

package com.crewmeister.cmcodingchallenge.currency.controller;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionDto;
import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateDto;
import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRequestDto;
import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CurrencyControllerTests {

    @Autowired
    private CurrencyController currencyController;

    @Test
    void testWhenControllerGetCurrenciesThenSixCurrenciesReturned() {
        // Given

        // When
        ResponseEntity<List<CurrencyDto>> response = currencyController.getCurrencies();

        // Then
        assertNotNull(response.getBody());
        assertEquals(6, response.getBody().size());
    }

    @Test
    void testWhenControllerGetConversionRatesForCurrencyUsdThen6978ConversionRatesReturned() {
        // Given
        String currencyCodeUsd = "USD";
        Integer expectedConversionRates = 6978;
        // When
        ResponseEntity<List<ConversionRateDto>> response = currencyController.getConversionRatesForCurrency(currencyCodeUsd);

        // Then
        assertNotNull(response.getBody());
        assertEquals(expectedConversionRates, response.getBody().size());
    }

    @Test
    void testWhenControllerGetConversionRateForCurrencyArsThenCurrencyNotFound() {
        // Given
        String currencyCodeArs= "ARS";
        // When
        assertThrows(ResponseStatusException.class, () -> {
            ResponseEntity<List<ConversionRateDto>> response = currencyController.getConversionRatesForCurrency(currencyCodeArs);
            // Then exception thrown
            assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        });
    }

    @Test
    void testWhenControllerGetConversionRateForCurrencyUsdOn20260316Then1p16ConversionRateReturned() {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "2026-03-16";
        Double expectedConversionRate = 1.1478;
        // When
        ResponseEntity<ConversionRateDto> response = currencyController.getConversionRateForCurrencyOnGivenDate(currencyCodeUsd, givenDate);

        // Then
        assertNotNull(response.getBody());
        assertEquals(expectedConversionRate, response.getBody().getRate());
    }

    @Test
    void testWhenControllerGetConversionRateForCurrencyUsdOnInvalidDateFormatThenBadRequestReturned() {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "abcdefg";
        // When
        assertThrows(ResponseStatusException.class, () -> {
            ResponseEntity<ConversionRateDto> response = currencyController.getConversionRateForCurrencyOnGivenDate(
                    currencyCodeUsd,
                    givenDate
            );
            assertEquals(HttpStatusCode.valueOf(400), response.getStatusCode());
        });

    }

    @Test
    void testWhenControllerGetConversionRateForCurrencyUsdOn20260315ThenConversionRateNotFound() {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "2026-03-15";
        // When
        assertThrows(ResponseStatusException.class, () -> {
            ResponseEntity<ConversionRateDto> response = currencyController.getConversionRateForCurrencyOnGivenDate(currencyCodeUsd, givenDate);
            // Then exception thrown
            assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        });
    }

    @Test
    void testWhenControllerGetConversionRateForCurrencyArsOn20260315ThenConversionRateNotFound() {
        // Given
        String currencyCodeArs = "ARS";
        String givenDate = "2026-03-15";
        // When
        assertThrows(ResponseStatusException.class, () -> {
            ResponseEntity<ConversionRateDto> response = currencyController.getConversionRateForCurrencyOnGivenDate(currencyCodeArs, givenDate);
            // Then exception thrown
            assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        });
    }

    @Test
    void testWhenControllerConvert2500UsdToEurOn20250316ThenConvertedTo2178p08Eur() {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "2026-03-16";
        Double usdAmount = 2500.0;
        ConversionRequestDto requestDto = new ConversionRequestDto(usdAmount);
        Double expectedConvertedAmount = 2178.08;

        // When
        ResponseEntity<ConversionDto> response = currencyController.convertToEurosOnGivenDay(
                currencyCodeUsd,
                givenDate,
                requestDto
        );

        // Then
        assertNotNull(response.getBody());
        assertEquals(expectedConvertedAmount, response.getBody().getAmountInEuros());
    }

    @Test
    void testWhenControllerConvert2500UsdToEurOn20250315ThenConversionRateNotFound() {
        // Given
        String currencyCodeUsd = "USD";
        String givenDate = "2026-03-15";
        Double usdAmount = 2500.0;
        ConversionRequestDto requestDto = new ConversionRequestDto(usdAmount);

        // When
        assertThrows(ResponseStatusException.class, () -> {
            ResponseEntity<ConversionDto> response = currencyController.convertToEurosOnGivenDay(
                    currencyCodeUsd,
                    givenDate,
                    requestDto
            );
            assertEquals(HttpStatusCode.valueOf(404), response.getStatusCode());
        });
    }
}

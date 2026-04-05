package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CurrencyServiceTests {

    @Autowired
    private CurrencyService currencyService;

    @Test
    void testAvailableCurrenciesEqualToSix() {
        // Given the autowired currencyService

        // When
        List<Currency> currencyList = currencyService.getCurrencies();

        // Then
        assertEquals( 6,currencyList.size());
    }

    @Test
    void testUSDInAvailableCurrencies() {
        // Given
        String currencyCodeUsd = "USD";
        // When
        assertDoesNotThrow(() -> currencyService.getCurrencyByCode(currencyCodeUsd));

        // Then exception not thrown
    }

    @Test
    void testARSNotInAvailableCurrencies() {
        // Given
        String currencyCodeArs = "ARS";
        // When
        assertThrows(CurrencyNotFoundException.class,
                () -> currencyService.getCurrencyByCode(currencyCodeArs));

        // Then exception thrown
    }
}

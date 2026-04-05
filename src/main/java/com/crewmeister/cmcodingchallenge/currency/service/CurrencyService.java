package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;

import java.util.List;

/**
 * A Spring Boot service layer abstraction to retrieve currency information
 */
public interface CurrencyService {

    /**
     * Retrieves all available currencies in the application
     * @return a list of currencies by 3-letter code and name/description
     */
    List<Currency> getCurrencies();

    /**
     * Verifies that a given currency by `code` is available in the system.
     * Throws a `CurrencyNotFound` if the currency is not available
     * @param code The 3-letter currency code
     * @throws CurrencyNotFoundException when the given currency code is not available
     */
    void getCurrencyByCode(String code) throws CurrencyNotFoundException;

}

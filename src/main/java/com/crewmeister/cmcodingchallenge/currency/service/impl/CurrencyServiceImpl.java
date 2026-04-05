package com.crewmeister.cmcodingchallenge.currency.service.impl;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.currency.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A Spring Boot service layer abstraction to retrieve currency information
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    /**
     * Service implementation constructor that injects the JPA repository dependency
     * @param currencyRepository The JPA/CRUD repository used to interact with the database that stores currency information
     */
    public CurrencyServiceImpl(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    /**
     * Retrieves all available currencies in the application
     * @return a list of currencies by 3-letter code and name/description
     */
    @Override
    public List<Currency> getCurrencies() {
        return (List<Currency>) currencyRepository.findAll();
    }

    /**
     * Verifies that a given currency by `code` is available in the system.
     * Throws a `CurrencyNotFound` if the currency is not available
     * @param code The 3-letter currency code
     * @throws CurrencyNotFoundException when the given currency code is not available
     */
    @Override
    public void getCurrencyByCode(String code) throws CurrencyNotFoundException {

        Currency currency = currencyRepository.getCurrencyByCurrencyCode(code);
        if(currency == null) {
            throw new CurrencyNotFoundException();
        }
    }
}

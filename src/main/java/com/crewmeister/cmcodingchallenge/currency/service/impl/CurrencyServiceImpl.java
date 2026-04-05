package com.crewmeister.cmcodingchallenge.currency.service.impl;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.repository.CurrencyRepository;
import com.crewmeister.cmcodingchallenge.currency.service.CurrencyService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private final CurrencyRepository currencyRepository;

    public CurrencyServiceImpl(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    @Override
    public List<Currency> getCurrencies() {
        return (List<Currency>) currencyRepository.findAll();
    }

    @Override
    public void getCurrencyByCode(String code) throws CurrencyNotFoundException {

        Currency currency = currencyRepository.getCurrencyByCurrencyCode(code);
        if(currency == null) {
            throw new CurrencyNotFoundException();
        }
    }
}

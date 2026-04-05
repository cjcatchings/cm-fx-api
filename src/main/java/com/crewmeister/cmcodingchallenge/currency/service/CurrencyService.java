package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;

import java.util.List;

public interface CurrencyService {

    List<Currency> getCurrencies();

    void getCurrencyByCode(String code) throws CurrencyNotFoundException;

}

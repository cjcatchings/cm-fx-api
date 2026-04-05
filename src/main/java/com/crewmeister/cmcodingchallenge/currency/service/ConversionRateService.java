package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;

import java.text.ParseException;
import java.util.List;

public interface ConversionRateService {

    List<ConversionRate> getByCurrencyCode(String code);

    ConversionRate getByCurrencyCodeAndDate(String code, String date) throws ConversionRateNotFoundException, ParseException;

    Double convertToEuros(Double sourceAmount, Double rate);
}

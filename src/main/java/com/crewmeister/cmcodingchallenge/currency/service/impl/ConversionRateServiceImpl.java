package com.crewmeister.cmcodingchallenge.currency.service.impl;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.repository.ConversionRateRepository;
import com.crewmeister.cmcodingchallenge.currency.service.ConversionRateService;
import com.crewmeister.cmcodingchallenge.currency.util.FormatUtil;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

@Service
public class ConversionRateServiceImpl implements ConversionRateService {

    private final ConversionRateRepository conversionRateRepository;

    public ConversionRateServiceImpl(ConversionRateRepository conversionRateRepository) {
        this.conversionRateRepository = conversionRateRepository;
    }

    @Override
    public List<ConversionRate> getByCurrencyCode(String code) {
        return conversionRateRepository.getConversionRatesByCurrency_CurrencyCodeOrderByDateDesc(code);
    }

    @Override
    public ConversionRate getByCurrencyCodeAndDate(String code, String date) throws ConversionRateNotFoundException, ParseException {
        Date dateObj = FormatUtil.convertDateStringToCalendar(date);
        ConversionRate rate = conversionRateRepository.getConversionRateByCurrency_CurrencyCodeAndDate(code, dateObj);
        if(rate == null) {
            throw new ConversionRateNotFoundException();
        }
        return rate;
    }

    @Override
    public Double convertToEuros(Double sourceAmount, Double rate) {
        Double convertedAmount = sourceAmount / rate;
        return Math.round(convertedAmount * 100.0) / 100.0;
    }
}

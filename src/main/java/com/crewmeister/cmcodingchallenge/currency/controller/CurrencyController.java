package com.crewmeister.cmcodingchallenge.currency.controller;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionDto;
import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateDto;
import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRequestDto;
import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyDto;
import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.exception.CurrencyNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.mapper.ConversionMapper;
import com.crewmeister.cmcodingchallenge.currency.mapper.ConversionRateMapper;
import com.crewmeister.cmcodingchallenge.currency.mapper.CurrencyMapper;
import com.crewmeister.cmcodingchallenge.currency.service.ConversionRateService;
import com.crewmeister.cmcodingchallenge.currency.service.CurrencyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Currency Controller
 */
@RestController()
@RequestMapping("/api")
public class CurrencyController {

    private static final Logger log = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;
    private final ConversionRateService conversionRateService;

    public CurrencyController(
            CurrencyService currencyService,
            ConversionRateService conversionRateService
    ) {
        this.currencyService = currencyService;
        this.conversionRateService = conversionRateService;
    }

    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        log.debug("Entering getCurrencies");
        List<Currency> currencies = new ArrayList<>(currencyService.getCurrencies());
        List<CurrencyDto> currenciesDto = currencies.stream().map(CurrencyMapper.INSTANCE::currencyToCurrencyDto).toList();
        log.debug("Exiting getCurrencies");
        return new ResponseEntity<>(currenciesDto, HttpStatus.OK);
    }

    @GetMapping("/currencies/{code}")
    public ResponseEntity<List<ConversionRateDto>> getConversionRatesForCurrency(@PathVariable String code) {
        log.debug("Entering getConversionRatesForCurrency");
        try {
            currencyService.getCurrencyByCode(code);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Currency '%s' not found", code));
        }
        List<ConversionRate> conversionRates = new ArrayList<>(conversionRateService.getByCurrencyCode(code));
        List<ConversionRateDto> conversionRatesDto = conversionRates.stream().map(ConversionRateMapper.INSTANCE::conversionRateToConversionRateDto).toList();
        log.debug("Exiting getConversionRatesForCurrency");
        return new ResponseEntity<>(conversionRatesDto, HttpStatus.OK);
    }

    @GetMapping("/currencies/{code}/{date}")
    public ResponseEntity<ConversionRateDto> getConversionRateForCurrencyOnGivenDate(@PathVariable String code, @PathVariable String date) {
        log.debug("Entering getConversionRateForCurrencyOnGivenDate");
        try {
            currencyService.getCurrencyByCode(code);
        } catch (CurrencyNotFoundException nfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Currency '%s' not found", code));
        }
        ConversionRate rate;
        try {
            rate = conversionRateService.getByCurrencyCodeAndDate(code, date);
        } catch (ConversionRateNotFoundException nfe) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Currency '%s' conversion rate not found for date '%s'", code, date));
        } catch (ParseException pe) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, String.format("Invalid date format: '%s'", date));
        }
        ConversionRateDto conversionRateDto = ConversionRateMapper.INSTANCE.conversionRateToConversionRateDto(rate);
        log.debug("Exiting getConversionRateForCurrencyOnGivenDate");
        return new ResponseEntity<>(conversionRateDto, HttpStatus.OK);
    }

    @GetMapping("/currencies/{code}/{date}/convert")
    public ResponseEntity<ConversionDto> convertToEurosOnGivenDay(
            @PathVariable String code,
            @PathVariable String date,
            @RequestBody ConversionRequestDto conversionRequestDto) {
        log.debug("Entering convertToEurosOnGivenDay");
        ConversionRateDto rateDto = getConversionRateForCurrencyOnGivenDate(code, date).getBody();
        if(rateDto == null){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Currency '%s' conversion rate not found for date '%s'", code, date));
        }
        Double amountInEuros = conversionRateService.convertToEuros(conversionRequestDto.getSourceAmount(), rateDto.getRate());
        ConversionDto conversionDto = ConversionMapper.conversionToDto(rateDto.getRate(), amountInEuros);
        log.debug("Exiting convertToEurosOnGivenDay");
        return new ResponseEntity<>(conversionDto, HttpStatus.OK);
    }
}

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
 * The Spring Boot REST controller entrypoint for the Euro currency exchange API.  The API contains the following four (4)
 * endpoints:
 * GET - /api/currencies - returns a list of available currencies in the API by 3-letter code and name
 * GET - /api/currencies/{code} - returns daily exchange rates for a given currency by `code` to Euros (EUR)
 * GET - /api/currencies/{code}/{date} - returns the exchange rate for a given currency by `code` on a given `date` in yyyy-MM-dd format
 * GET - /api/currencies/{code}/{date}/conert - converts an amount of a currency given by `code` on a given `date` to Euros
 */
@RestController()
@RequestMapping("/api")
public class CurrencyController {

    private static final Logger log = LoggerFactory.getLogger(CurrencyController.class);

    private final CurrencyService currencyService;
    private final ConversionRateService conversionRateService;

    /**
     * Constructor for the
     *
     * @param currencyService - The Spring Boot service layer abstraction that retrieves currency data
     * @param conversionRateService - The Spring Boot service layer abstraction that retrieves conversion rate data
     */
    public CurrencyController(
            CurrencyService currencyService,
            ConversionRateService conversionRateService
    ) {
        this.currencyService = currencyService;
        this.conversionRateService = conversionRateService;
    }

    /**
     * Retrieves the set of currencies by 3-letter code and name that are available in this API
     * @return A list of currency objects with 3-letter code and name/description
     */
    @GetMapping("/currencies")
    public ResponseEntity<List<CurrencyDto>> getCurrencies() {
        log.debug("Entering getCurrencies");
        List<Currency> currencies = new ArrayList<>(currencyService.getCurrencies());
        List<CurrencyDto> currenciesDto = currencies.stream().map(CurrencyMapper.INSTANCE::currencyToCurrencyDto).toList();
        log.debug("Exiting getCurrencies");
        return new ResponseEntity<>(currenciesDto, HttpStatus.OK);
    }

    /**
     * Retrieves daily conversion rates from Euros to a given currency by `code`.  Returns an HTTP 404 response if
     * the currency is not available in the API.
     * Returns an HTTP 404 (not found) response if the given currency code is not available in the API
     * @param code The 3-letter currency code to retrieve daily exchange rates (from EUR to given currency)
     * @return A list of conversion rate objects by date (yyyy-MM-dd) and rate from Euros to the given currency
     */
    @GetMapping("/currencies/{code}")
    public ResponseEntity<List<ConversionRateDto>> getConversionRatesForCurrency(
            @PathVariable String code,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        log.debug("Entering getConversionRatesForCurrency");
        try {
            currencyService.getCurrencyByCode(code);
        } catch (CurrencyNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Currency '%s' not found", code));
        }
        List<ConversionRate> conversionRates = new ArrayList<>(
                conversionRateService.getByCurrencyCode(code, from, to)
        );
        List<ConversionRateDto> conversionRatesDto = conversionRates.stream().map(ConversionRateMapper.INSTANCE::conversionRateToConversionRateDto).toList();
        log.debug("Exiting getConversionRatesForCurrency");
        return new ResponseEntity<>(conversionRatesDto, HttpStatus.OK);
    }

    /**
     * Retrieves the conversion rate from Euros to a given currency (by `code`) on a given `date` (in yyyy-MM-dd) format.
     * Returns an HTTP 404 (not found) response if the provided currency is not available in the API or if there is no conversion rate
     * for the given date (due to the date being on a weekend or holiday).
     * Returns an HTTP 400 (bad request) response if the date format is invalid.
     * @param code The 3-letter currency code to retrieve daily exchange rates (from EUR to given currency)
     * @param date The date on which to retrieve the conversion rate from Euros to the given currency
     * @return A single conversion rate object with the given date and the conversion rate from Euros to the given currency on that day
     */
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

    /**
     * Converts a given numeric amount of a currency by `code` on a given `date` (in yyyy-MM-dd format) into Euros.  Provides the conversion rate from Euros to given currency used.
     * Returns an HTTP 404 (not found) response if the provided currency is not available in the API or if there is no conversion rate
     * for the given date (due to the date being on a weekend or holiday).
     * Returns an HTTP 400 (bad request) response if the date format or requested currency amount is invalid.
     * @param code The 3-letter currency code to retrieve daily exchange rates (from EUR to given currency)
     * @param date The date on which to retrieve the conversion rate from Euros to the given currency
     * @param conversionRequestDto A JSON object in the request body that provides the `sourceAmount` in the given currency to convert to Euros.
     * @return The conversion rate (from Euros to given currency) used to calculate the amount of Euros calculated as well as the amount in Euros.
     */
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

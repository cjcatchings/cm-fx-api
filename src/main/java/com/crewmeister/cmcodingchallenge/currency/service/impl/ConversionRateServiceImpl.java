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

/**
 * A Spring Boot service layer abstraction to retrieve conversion rate information
 */
@Service
public class ConversionRateServiceImpl implements ConversionRateService {

    private final ConversionRateRepository conversionRateRepository;

    /**
     * Service implementation constructor that injects the JPA repository dependency
     * @param conversionRateRepository The JPA/CRUD repository used to interact with the database that stores conversion rate information
     */
    public ConversionRateServiceImpl(ConversionRateRepository conversionRateRepository) {
        this.conversionRateRepository = conversionRateRepository;
    }

    /**
     * Retrieves daily conversion rates for a given currency by currency code
     * @param code The 3-letter currency code for which to retrieve conversion rates (from EUR to given currency)
     * @return A list of conversion rates for the given currency
     */
    @Override
    public List<ConversionRate> getByCurrencyCode(String code) {
        return conversionRateRepository.getConversionRatesByCurrency_CurrencyCodeOrderByDateDesc(code);
    }

    /**
     * Retrieves the conversion rate for a given currency by currency code on a given date in (yyyy-MM-dd format).
     * @param code The 3-letter currency code for which to retrieve conversion rates (from EUR to given currency)
     * @param date The date for which to retrieve the conversion rate from EUR to given currency
     * @return The conversion rate for the given currency on the given date
     * @throws ConversionRateNotFoundException If the conversion rate for the given currency is not found
     * (either the currency is not available in the system or there is no conversion rate for the given day)
     * @throws ParseException If the given date is in an invalid format (not yyyy-MM-dd)
     */
    @Override
    public ConversionRate getByCurrencyCodeAndDate(String code, String date) throws ConversionRateNotFoundException, ParseException {
        Date dateObj = FormatUtil.convertDateStringToCalendar(date);
        ConversionRate rate = conversionRateRepository.getConversionRateByCurrency_CurrencyCodeAndDate(code, dateObj);
        if(rate == null) {
            throw new ConversionRateNotFoundException();
        }
        return rate;
    }

    /**
     * Converts a given source amount and a rate into a target amount by dividing the amount by the rate.
     * Rounds the returned amount to the nearest 2 decimal places.
     * @param sourceAmount  The source amount (in a given currency)
     * @param rate The conversion rate (to be divided by)
     * @return The target amount (in Euros)
     */
    @Override
    public Double convertToEuros(Double sourceAmount, Double rate) {
        Double convertedAmount = sourceAmount / rate;
        return Math.round(convertedAmount * 100.0) / 100.0;
    }
}

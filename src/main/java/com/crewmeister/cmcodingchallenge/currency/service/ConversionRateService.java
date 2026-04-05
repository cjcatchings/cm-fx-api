package com.crewmeister.cmcodingchallenge.currency.service;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;

import java.text.ParseException;
import java.util.List;

/**
 * A Spring Boot service layer abstraction to retrieve conversion rate information
 */
public interface ConversionRateService {

    /**
     * Retrieves daily conversion rates for a given currency by currency code
     * @param code The 3-letter currency code for which to retrieve conversion rates (from EUR to given currency)
     * @return A list of conversion rates for the given currency
     */
    List<ConversionRate> getByCurrencyCode(String code);

    /**
     * Retrieves the conversion rate for a given currency by currency code on a given date in (yyyy-MM-dd format).
     * @param code The 3-letter currency code for which to retrieve conversion rates (from EUR to given currency)
     * @param date The date for which to retrieve the conversion rate from EUR to given currency
     * @return The conversion rate for the given currency on the given date
     * @throws ConversionRateNotFoundException If the conversion rate for the given currency is not found
     * (either the currency is not available in the system or there is no conversion rate for the given day)
     * @throws ParseException If the given date is in an invalid format (not yyyy-MM-dd)
     */
    ConversionRate getByCurrencyCodeAndDate(String code, String date) throws ConversionRateNotFoundException, ParseException;

    /**
     * Converts a given source amount and a rate into a target amount by dividing the amount by the rate.
     * Rounds the returned amount to the nearest 2 decimal places.
     * @param sourceAmount  The source amount (in a given currency)
     * @param rate The conversion rate (to be divided by)
     * @return The target amount (in Euros)
     */
    Double convertToEuros(Double sourceAmount, Double rate);
}

package com.crewmeister.cmcodingchallenge.currency.service.impl;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.exception.ConversionRateNotFoundException;
import com.crewmeister.cmcodingchallenge.currency.repository.ConversionRateRepository;
import com.crewmeister.cmcodingchallenge.currency.service.ConversionRateService;
import com.crewmeister.cmcodingchallenge.currency.util.FormatUtil;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import static com.crewmeister.cmcodingchallenge.currency.repository.spec.ConversionRateSpecification.*;

/**
 * A Spring Boot service layer abstraction to retrieve conversion rate information
 */
@Service
public class ConversionRateServiceImpl implements ConversionRateService {

    @Value("${crewmeister.getrates.maxresults:#{null}}")
    private Integer maxResultsForGetRates;

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
     * @param from The earliest date (in yyyy-MM-dd) from which to retrieve conversion rates for this currency
     * @param to The latest date (in yyyy-MM-dd) from which to retrieve conversion rates for this currency
     * @return A list of conversion rates for the given currency
     */
    @Override
    public List<ConversionRate> getByCurrencyCode(String code, String from, String to) {
        Date fromDateObj = FormatUtil.convertDateStringToDateOrNull(from);
        Date toDateObj = calculateToDateIfOnlyFromDateProvided(
                fromDateObj,
                FormatUtil.convertDateStringToDateOrNull(to)
        );

        Specification<ConversionRate> spec = Specification
                .where(hasCurrencyCode(code))
                .and(fromDate(fromDateObj))
                .and(toDate(toDateObj));

        if (maxResultsForGetRates != null) {
            Pageable pageable = PageRequest.of(
                    0,
                    maxResultsForGetRates,
                    Sort.by(Sort.Direction.DESC, "date")
            );
            return conversionRateRepository.findAll(spec, pageable);
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "date");
        return conversionRateRepository.findAll(spec, sort);
    }

    /**
     * If an "from" date is provided with no to date, this method will calculate a
     * to date to provide maxResultsForGetRates ConversionRates from the provided
     * fromDate
     * @param fromDate
     * @param toDate
     * @return
     */
    private Date calculateToDateIfOnlyFromDateProvided(Date fromDate, Date toDate) {
        if (fromDate == null || toDate != null || maxResultsForGetRates == null) {
            return toDate;
        }
        return DateUtils.addDays(fromDate, maxResultsForGetRates);
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
        Date dateObj = FormatUtil.convertDateStringToDate(date);
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

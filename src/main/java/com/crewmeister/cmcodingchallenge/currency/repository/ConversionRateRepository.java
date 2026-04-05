package com.crewmeister.cmcodingchallenge.currency.repository;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A JPA CRUD repository that provides method abstractions for the ConversionRate table
 */
@Repository
public interface ConversionRateRepository extends CrudRepository<ConversionRate, Long> {

    /**
     * SELECT * FROM CONVERSION_RATE WHERE CURRENCY_CODE = {code} ORDER BY DATE DESC;
     * @param code The 3-letter currency code
     * @return The daily conversion rates for the given currency
     */
    List<ConversionRate> getConversionRatesByCurrency_CurrencyCodeOrderByDateDesc(String code);

    /**
     * SELECT * FROM CONVERSION_RATE WHERE CURRENCY_CODE = {code} AND DATE = {date};
     * Asserts that at most one record is returned
     * @param code The 3-letter currency code
     * @param date The date for which to retrieve the conversion rate for the given currency
     * @return
     */
    ConversionRate getConversionRateByCurrency_CurrencyCodeAndDate(String code, Date date);

    /**
     * DELETE FROM CONVERSION_RATE
     * Used in unit testing for the Spring Batch job that loads the currency + conversion rate data
     */
    void deleteAll();
}

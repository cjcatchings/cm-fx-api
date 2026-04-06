package com.crewmeister.cmcodingchallenge.currency.repository;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
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
public interface ConversionRateRepository extends JpaRepository<ConversionRate, Long> {

    /**
     * SELECT * FROM CONVERSION_RATE WHERE CURRENCY_CODE = {code}
     * with optional AND statements
     * AND DATE > {givenDateInSpec}
     * AND DATE < {givenDateInSpec}
     * SORT BY {sort}
     * @param spec Query specification to filter results
     * @param sort Sort direction (typically DESC)
     * @return A list of filtered conversion rates
     */
    List<ConversionRate> findAll(Specification<ConversionRate> spec, Sort sort);

    /**
     * SELECT * FROM CONVERSION_RATE WHERE CURRENCY_CODE = {code}
     * with optional AND statements
     * AND DATE > {givenDateInSpec}
     * AND DATE < {givenDateInSpec}
     * SORT BY {pageable}
     * OFFSET 0
     * @param spec Query specification to filter results
     * @param pageable Pageable object that handles sorting and paging (offset/limit)
     * @return A list of filtered conversion rates
     */
    List<ConversionRate> findAll(Specification<ConversionRate> spec, Pageable pageable);

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
     * @return The conversion rate for the given date and code
     */
    ConversionRate getConversionRateByCurrency_CurrencyCodeAndDate(String code, Date date);

    /**
     * DELETE FROM CONVERSION_RATE
     * Used in unit testing for the Spring Batch job that loads the currency + conversion rate data
     */
    void deleteAll();
}

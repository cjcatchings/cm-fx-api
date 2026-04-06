package com.crewmeister.cmcodingchallenge.currency.repository.spec;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.util.FormatUtil;
import org.springframework.data.jpa.domain.Specification;

import java.text.ParseException;
import java.util.Date;

/**
 * A Specification implementation for ConversionRate queries that may contain to and from
 * date range limits
 */
public class ConversionRateSpecification {

    /**
     * Filters ConversionRate rows by Currency
     * @param currencyCode the 3-letter currency code to retrieve results from
     * @return an updated Specification to provide the JPA repository method
     */
    public static Specification<ConversionRate> hasCurrencyCode(String currencyCode) {
        return (root, query, cb) ->
                cb.equal(root.get("currency").get("currencyCode"), currencyCode);
    }

    /**
     * Returns ConversionRate dates from a specified date or later
     * @param fromDate the earliest date from which to retrieve conversion rates
     * @return an updated Specification to provide the JPA repository method
     */
    public static Specification<ConversionRate> fromDate(Date fromDate) {
        return (root, query, cb) ->
                fromDate == null ? null : cb.greaterThanOrEqualTo(root.get("date"), fromDate);
    }

    /**
     * Returns ConversionRate dates up to a specified date
     * @param toDate the latest date from which to retrieve conversion rates
     * @return an updated Specification to provide the JPA repository method
     */
    public static Specification<ConversionRate> toDate(Date toDate) {
        return (root, query, cb) ->
                toDate == null ? null : cb.lessThanOrEqualTo(root.get("date"), toDate);
    }
}
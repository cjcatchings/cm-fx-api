package com.crewmeister.cmcodingchallenge.currency.repository;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * A JPA CRUD repository that provides method abstractions for the Currency table
 */
@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {

    /**
     * SELECT * FROM CURRENCY WHERE CURRENCY_CODE = {code};
     * Asserts that at most one record is returned
     * @param code The 3-letter currency code to retrieve
     * @return The currency (including name and code
     */
    Currency getCurrencyByCurrencyCode(String code);

    /**
     * DELETE FROM CURRENCY
     * Used in unit testing for the Spring Batch job that loads the currency + conversion rate data
     */
    void deleteAll();
}

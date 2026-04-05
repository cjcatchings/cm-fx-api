package com.crewmeister.cmcodingchallenge.currency.repository;

import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrencyRepository extends CrudRepository<Currency, Long> {
    Currency getCurrencyByCurrencyCode(String code);

    void deleteAll();
}

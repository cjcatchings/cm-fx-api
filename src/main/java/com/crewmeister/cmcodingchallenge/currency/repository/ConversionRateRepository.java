package com.crewmeister.cmcodingchallenge.currency.repository;

import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

@Repository
public interface ConversionRateRepository extends CrudRepository<ConversionRate, Long> {
    List<ConversionRate> getConversionRatesByCurrency_CurrencyCodeOrderByDateDesc(String code);

    ConversionRate getConversionRateByCurrency_CurrencyCodeAndDate(String code, Date date);

    void deleteAll();
}

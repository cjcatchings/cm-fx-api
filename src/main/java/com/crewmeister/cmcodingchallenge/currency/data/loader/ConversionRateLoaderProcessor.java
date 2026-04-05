package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.repository.CurrencyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.Map;

@Component
@StepScope
public class ConversionRateLoaderProcessor implements ItemProcessor<ConversionRateRecordDto, ConversionRate> {

    private final Map<String, Currency> currencyCache = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(ConversionRateLoaderProcessor.class);

    private final CurrencyRepository currencyRepository;

    public ConversionRateLoaderProcessor(CurrencyRepository currencyRepository){
        this.currencyRepository = currencyRepository;
    }

    private Currency resolveCurrency(String code) {
        return currencyCache.computeIfAbsent(code, currencyRepository::getCurrencyByCurrencyCode);
    }

    @Override
    public ConversionRate process(ConversionRateRecordDto record) {
        if(record.getRate() == null || record.getRate().isBlank()) {
            log.warn("Invalid rate for currency {} on date {}.  Discarding the record.",
                    record.getCurrencyCode(),
                    record.getDate())
            ;
            return null;
        } else if (record.getRate().equals(".")) {
            return null;
        }

        LocalDate date;
        Double rate;
        try {
            date = LocalDate.parse(record.getDate(), DateTimeFormatter.ISO_LOCAL_DATE);
            rate = Double.parseDouble(record.getRate());
        } catch (DateTimeParseException | NumberFormatException e) {
            log.warn("Could not parse either date '{}' or rate '{}'.  Discarding the record.", record.getDate(), record.getRate());
            return null;
        }

        Currency resolvedCurrency = resolveCurrency(record.getCurrencyCode());
        if (resolvedCurrency == null){
            log.warn("Record on '{}' with rate '{}' has currency '{}' which is not available.  Discarding the record.",
                    record.getDate(),
                    record.getRate(),
                    record.getCurrencyCode());
            return null;
        }
        ConversionRate newRate = new ConversionRate();
        newRate.setCurrency(resolvedCurrency);
        newRate.setDate(java.sql.Date.valueOf(date));
        newRate.setRate(rate);
        return newRate;
    }
}

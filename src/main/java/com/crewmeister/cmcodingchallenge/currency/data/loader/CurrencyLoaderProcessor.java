package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyRecordDto;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * A Spring Batch Processor that converts `CurrencyRecordDto` CSV records into `Currency` entities to load in the database
 * at startup
 */
@Component
@StepScope
public class CurrencyLoaderProcessor implements ItemProcessor<CurrencyRecordDto, Currency> {

    private static final Logger log = LoggerFactory.getLogger(CurrencyLoaderProcessor.class);

    /**
     * Processes the {@code CurrencyRecord} CSV record into a {@code Currency} entity.
     * @param record to be processed, never {@code null}.
     * @return the {@code Currency} entity to be loaded into the database
     */
    @Override
    public Currency process(CurrencyRecordDto record) {
        String code = record.getCode();
        String name = record.getName();

        log.debug("Loading new currency record with code '{}' and name '{}'.", code, name);

        Currency newCurrency = new Currency();
        newCurrency.setCurrencyName(name);
        newCurrency.setCurrencyCode(code);
        return newCurrency;
    }
}

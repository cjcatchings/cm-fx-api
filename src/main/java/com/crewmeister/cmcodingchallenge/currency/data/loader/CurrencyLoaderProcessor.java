package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyRecordDto;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@StepScope
public class CurrencyLoaderProcessor implements ItemProcessor<CurrencyRecordDto, Currency> {

    private static final Logger log = LoggerFactory.getLogger(CurrencyLoaderProcessor.class);

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

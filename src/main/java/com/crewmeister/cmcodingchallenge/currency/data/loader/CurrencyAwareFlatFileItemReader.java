package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import java.util.Objects;

/**
 * A Spring Batch CSV file reader that is "aware" of existing resources ({@code Currency} entities)
 * Retrieves the 3-letter currency code from the name of the CSV file containing the conversion rates
 */
public class CurrencyAwareFlatFileItemReader
        extends FlatFileItemReader<ConversionRateRecordDto>
        implements ResourceAwareItemReaderItemStream<ConversionRateRecordDto> {

    @Autowired
    private CurrencyAwareLineMapper currencyAwareLineMapper;

    /**
     * Sets the {@code CurrencyCode} for the autowired LineMapper to load CSV records with the given currency
     * @param resource Resource (CSV file) to read records from
     */
    @Override
    public void setResource(@NonNull Resource resource){
        super.setResource(resource);
        String filename = Objects.requireNonNull(resource.getFilename());
        String code = filename.substring(8, 11).toUpperCase();
        currencyAwareLineMapper.setCurrencyCode(code);
    }
}

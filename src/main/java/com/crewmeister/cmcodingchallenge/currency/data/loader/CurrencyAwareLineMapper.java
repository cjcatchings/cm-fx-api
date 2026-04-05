package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import lombok.Setter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

/**
 * A Spring Batch Line Mapper that is "aware" of the currency so that {@code ConversionRate}
 * lines can be mapped with the given currency
 */
@Setter
@Component
public class CurrencyAwareLineMapper implements LineMapper<ConversionRateRecordDto> {

    private String currencyCode;

    /**
     * Maps a given CSV file line to a {@code ConversionRateRecordDto} with the current currency
     * @param line to be mapped
     * @param lineNumber of the current line
     * @return the {@code ConversionRateRecordDto} object to be processed in the {@code ItemProcessor}
     */
    @Override
    @NonNull
    public ConversionRateRecordDto mapLine(String line, int lineNumber) {
        String[] fields = line.split(",");
        ConversionRateRecordDto record = new ConversionRateRecordDto();
        record.setCurrencyCode(currencyCode);
        record.setDate(fields[0].trim());
        record.setRate(fields[1].trim());
        return record;
    }

}

package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import lombok.Setter;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Setter
@Component
public class CurrencyAwareLineMapper implements LineMapper<ConversionRateRecordDto> {

    private String currencyCode;

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

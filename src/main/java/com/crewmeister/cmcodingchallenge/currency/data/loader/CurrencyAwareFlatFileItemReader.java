package com.crewmeister.cmcodingchallenge.currency.data.loader;

import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.ResourceAwareItemReaderItemStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;

import java.util.Objects;

public class CurrencyAwareFlatFileItemReader
        extends FlatFileItemReader<ConversionRateRecordDto>
        implements ResourceAwareItemReaderItemStream<ConversionRateRecordDto> {

    @Autowired
    private CurrencyAwareLineMapper currencyAwareLineMapper;

    @Override
    public void setResource(@NonNull Resource resource){
        super.setResource(resource);
        String filename = Objects.requireNonNull(resource.getFilename());
        String code = filename.substring(8, 11).toUpperCase();
        currencyAwareLineMapper.setCurrencyCode(code);
    }
}

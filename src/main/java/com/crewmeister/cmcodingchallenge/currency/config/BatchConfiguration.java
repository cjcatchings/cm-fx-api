package com.crewmeister.cmcodingchallenge.currency.config;

import com.crewmeister.cmcodingchallenge.currency.data.loader.ConversionRateLoaderProcessor;
import com.crewmeister.cmcodingchallenge.currency.data.loader.CurrencyAwareFlatFileItemReader;
import com.crewmeister.cmcodingchallenge.currency.data.loader.CurrencyAwareLineMapper;
import com.crewmeister.cmcodingchallenge.currency.data.loader.CurrencyLoaderProcessor;
import com.crewmeister.cmcodingchallenge.currency.data.loader.listener.JobCompletionNotificationListener;
import com.crewmeister.cmcodingchallenge.currency.dto.ConversionRateRecordDto;
import com.crewmeister.cmcodingchallenge.currency.dto.CurrencyRecordDto;
import com.crewmeister.cmcodingchallenge.currency.entity.ConversionRate;
import com.crewmeister.cmcodingchallenge.currency.entity.Currency;
import com.crewmeister.cmcodingchallenge.currency.repository.ConversionRateRepository;
import com.crewmeister.cmcodingchallenge.currency.repository.CurrencyRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.MultiResourceItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfiguration {

    private final CurrencyRepository currencyRepository;
    private final ConversionRateRepository conversionRateRepository;
    private final CurrencyAwareLineMapper currencyAwareLineMapper;

    public BatchConfiguration(
            CurrencyRepository currencyRepository,
            ConversionRateRepository conversionRateRepository,
            CurrencyAwareLineMapper currencyAwareLineMapper
    ) {
        this.currencyRepository = currencyRepository;
        this.conversionRateRepository = conversionRateRepository;
        this.currencyAwareLineMapper = currencyAwareLineMapper;
    }

    // Load Currencies

    @Bean
    @StepScope
    public FlatFileItemReader<CurrencyRecordDto> currencyFlatFileItemReader(@Value("classpath:currencies.csv") Resource resource) {
        FlatFileItemReader<CurrencyRecordDto> reader = new FlatFileItemReaderBuilder<CurrencyRecordDto>()
                .name("currency")
                .delimited()
                .delimiter(",")
                .names("code", "name")
                .targetType(CurrencyRecordDto.class)
                .linesToSkip(0)
                .build();
        reader.setResource(resource);
        return reader;
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<Currency> currencyWriter(){
        RepositoryItemWriter<Currency> writer = new RepositoryItemWriter<>();
        writer.setRepository(currencyRepository);
        writer.setMethodName("save");
        return writer;
    }

    // First step - import currencies

    @Bean
    public Step importCurrenciesStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            FlatFileItemReader<CurrencyRecordDto> reader,
            CurrencyLoaderProcessor processor,
            RepositoryItemWriter<Currency> writer
    ){
        return new StepBuilder("importCurrenciesStep", jobRepository)
                .<CurrencyRecordDto, Currency>chunk(5, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // Load Conversion Rates

    // Multi-resource item reader to read all CSV files in the resources/exchangerates folder

    @Bean
    @StepScope
    public MultiResourceItemReader<ConversionRateRecordDto> conversionRateReader(@Value("classpath:exchangerates/*.csv") Resource[] resources) {
        MultiResourceItemReader<ConversionRateRecordDto> reader = new MultiResourceItemReader<>();
        reader.setResources(resources);
        reader.setDelegate(currencyAwareConversionRateReader());
        return reader;
    }

    // Conversion rate reader that is "aware" of the available currencies loaded in the first job step

    @Bean
    @StepScope
    public CurrencyAwareFlatFileItemReader currencyAwareConversionRateReader() {
        CurrencyAwareFlatFileItemReader reader = new CurrencyAwareFlatFileItemReader();
        reader.setLineMapper(currencyAwareLineMapper);
        return reader;
    }

    // Line mapper to retrieve the Currency entity based on currency code for each conversion rate line read

    @Bean
    public CurrencyAwareLineMapper lineMapper(){
        return new CurrencyAwareLineMapper();
    }

    @Bean
    @StepScope
    public ItemProcessor<ConversionRateRecordDto, ConversionRate> conversionRateProcessor() {
        return new ConversionRateLoaderProcessor(currencyRepository);
    }

    @Bean
    @StepScope
    public RepositoryItemWriter<ConversionRate> conversionRateWriter() {
        RepositoryItemWriter<ConversionRate> writer = new RepositoryItemWriter<>();
        writer.setRepository(conversionRateRepository);
        writer.setMethodName("save");
        return writer;
    }

    // Second step - import conversion rates

    @Bean
    public Step importConversionRatesStep(
            JobRepository jobRepository,
            @Qualifier("transactionManager") PlatformTransactionManager transactionManager,
            MultiResourceItemReader<ConversionRateRecordDto> reader,
            ConversionRateLoaderProcessor processor,
            RepositoryItemWriter<ConversionRate> writer
    ) {
        return new StepBuilder("importConversionRatesStep", jobRepository)
                .<ConversionRateRecordDto, ConversionRate>chunk(50, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .faultTolerant()
                //.skip(Exception.class)
                .build();
    }

    // Spring Batch job to load currencies and conversion rates into the H2 database

    @Bean
    public Job importCurrenciesAndConversionRatesJob(
            JobRepository jobRepository,
            Step importCurrenciesStep,
            Step importConversionRatesStep,
            JobCompletionNotificationListener listener
    ) {
        return new JobBuilder("importCurrencyAndConversionRatesJob", jobRepository)
                .listener(listener)
                .start(importCurrenciesStep)
                .next(importConversionRatesStep)
                .build();
    }

}

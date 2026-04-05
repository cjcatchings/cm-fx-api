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

/**
 * A Spring Batch configuration for the job that loads currencies and conversion rates into the H2 database.
 */
@Configuration
public class BatchConfiguration {

    private final CurrencyRepository currencyRepository;
    private final ConversionRateRepository conversionRateRepository;
    private final CurrencyAwareLineMapper currencyAwareLineMapper;

    /**
     * Constructor used by Spring framework to inject repository and line mapper dependencies
     * @param currencyRepository - JPA repository in which to load currency data
     * @param conversionRateRepository - JPA repository in which to load conversion rate data
     * @param currencyAwareLineMapper - Spring Batch LineMapper to map a CSV file record (line) to a DTO
     */
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

    /**
     * A Spring batch flat file item reader to read CSV records from a given file resource
     * @param resource The file resource (in resources/currencies.csv)
     * @return A FlatFileItemReader that will read records from the given CSV file resource
     */
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

    /**
     * The Spring Batch JPA Repository item writer that will batch insert currencies into the H2 database
     * @return A RepositoryItemWriter to write Currency data into the database
     */
    @Bean
    @StepScope
    public RepositoryItemWriter<Currency> currencyWriter(){
        RepositoryItemWriter<Currency> writer = new RepositoryItemWriter<>();
        writer.setRepository(currencyRepository);
        writer.setMethodName("save");
        return writer;
    }

    // First step - import currencies

    /**
     * The first step in the `importCurrencyAndConversionRatesJob` that loads the Currency records into the database.
     * This step is a dependency on the conversion rate import step
     * @param jobRepository - Spring Batch JobRepository that loads/retrieves records from the Spring Batch tables
     * @param transactionManager - Manages database transactions in Spring Batch jobs
     * @param reader - The FlatFileItemReader that reads the records from the currencies.csv file
     * @param processor - The ItemProcessor that converts the CSV rows into the JPA entities
     * @param writer - The ItemWriter that batch writes Currency records into the database
     * @return The first Spring Batch Step of the `importCurrencyAndConversionRatesJob` Job
     */
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

    /**
     * A multi-resource item reader that reads multiple CSV files in resources/exchangerates to process conversion rates
     * @param resources A representation of multiple CSV file resources that contain exchange rate information
     * @return The ItemReader that will read all files in the resources/exchangerates folder
     */
    @Bean
    @StepScope
    public MultiResourceItemReader<ConversionRateRecordDto> conversionRateReader(@Value("classpath:exchangerates/*.csv") Resource[] resources) {
        MultiResourceItemReader<ConversionRateRecordDto> reader = new MultiResourceItemReader<>();
        reader.setResources(resources);
        reader.setDelegate(currencyAwareConversionRateReader());
        return reader;
    }

    // Conversion rate reader that is "aware" of the available currencies loaded in the first job step

    /**
     * A "Currency aware" CSV file reader that maps a conversion rate record to a currency given the currency code in the file name
     * @return The FlatFileItemReader that determines currency from the file names
     */
    @Bean
    @StepScope
    public CurrencyAwareFlatFileItemReader currencyAwareConversionRateReader() {
        CurrencyAwareFlatFileItemReader reader = new CurrencyAwareFlatFileItemReader();
        reader.setLineMapper(currencyAwareLineMapper);
        return reader;
    }

    // Line mapper to retrieve the Currency entity based on currency code for each conversion rate line read

    /**
     * A "Currency aware" LineMapper that builds the ConversionRate record from the data on the CSV line and currency code in the file name.
     * @return The Currency aware LineMapper
     */
    @Bean
    public CurrencyAwareLineMapper lineMapper(){
        return new CurrencyAwareLineMapper();
    }

    /**
     * Processes ConversionRate CSV records into the corresponding ConversionRate entity
     * @return the ItemProcessor that will convert CSV records into entity objects
     */
    @Bean
    @StepScope
    public ItemProcessor<ConversionRateRecordDto, ConversionRate> conversionRateProcessor() {
        return new ConversionRateLoaderProcessor(currencyRepository);
    }

    /**
     * The JPA Repository ItemWriter that will write ConversionRate records into the H2 database
     * @return The JPA Repository ItemWriter that will write ConversionRate records into the H2 database
     */
    @Bean
    @StepScope
    public RepositoryItemWriter<ConversionRate> conversionRateWriter() {
        RepositoryItemWriter<ConversionRate> writer = new RepositoryItemWriter<>();
        writer.setRepository(conversionRateRepository);
        writer.setMethodName("save");
        return writer;
    }

    // Second step - import conversion rates

    /**
     * The second step in the `importCurrencyAndConversionRatesJob` that loads the ConversionRate records into the database.
     * This step is a dependency on the conversion rate import step
     * @param jobRepository - Spring Batch JobRepository that loads/retrieves records from the Spring Batch tables
     * @param transactionManager - Manages database transactions in Spring Batch jobs
     * @param reader - The MultiResourceItemReader that reads the records from the collection of conversion rates CSV files in resources/exchangerates
     * @param processor - The ItemProcessor that converts the CSV rows into the JPA entities
     * @param writer - The ItemWriter that batch writes ConversionRate records into the database
     * @return The second Spring Batch Step of the `importCurrencyAndConversionRatesJob` Job
     */
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

    /**
     * The Spring Batch Job that processes the two steps required to load the currency and conversion rate
     * data into the database
     * @param jobRepository - The Spring Batch JobRepository that manages transactions in the Batch tables
     * @param importCurrenciesStep - The first step that loads Currencies
     * @param importConversionRatesStep - The second step that loads Conversion rates
     * @param listener - The listener that reports Job completion or failure
     * @return The Spring Batch Job that will load records into the ddatabase
     */
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

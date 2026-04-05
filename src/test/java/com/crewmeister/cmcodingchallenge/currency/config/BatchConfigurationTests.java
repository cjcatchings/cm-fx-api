package com.crewmeister.cmcodingchallenge.currency.config;

import com.crewmeister.cmcodingchallenge.currency.repository.ConversionRateRepository;
import com.crewmeister.cmcodingchallenge.currency.repository.CurrencyRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.annotation.BeforeJob;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@SpringBatchTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
public class BatchConfigurationTests {

    @Autowired
    private CurrencyRepository currencyRepository;

    @Autowired
    private ConversionRateRepository conversionRateRepository;

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @BeforeEach
    void emptyTablesBeforeTestJob() {
        conversionRateRepository.deleteAll();
        currencyRepository.deleteAll();
    }

    @Test
    void testWhenImportCurrencyAndConversionRatesJobThenJobCompleted() throws Exception{
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}

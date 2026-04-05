package com.crewmeister.cmcodingchallenge.currency.data.loader.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.stereotype.Component;

/**
 * A generic Spring Batch Job Notification Listener that reports the database load job
 * as completed or failed
 */
@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("Currency and conversation rate import finished successfully.");
        } else {
            log.error("Job finished with status: {}", jobExecution.getStatus());
        }
    }
}
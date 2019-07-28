package com.batch.util;

import java.util.Date;
import java.util.logging.Logger;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsycJobs {

	private static final Logger logger = Logger.getLogger(AsycJobs.class.getName());

	@Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job importIndustryJob;
    
	@Async
	public void processBatch(String n_name) {
		logger.info("Processing "+n_name);
		try {
			JobParameters jobParameters = new JobParametersBuilder()
					.addLong("time", new Date().getTime())
					.addString("csvPath", n_name)
					.toJobParameters();
			jobLauncher.run(importIndustryJob, jobParameters);
		} catch (JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException
				| JobParametersInvalidException e) {
			e.printStackTrace();
		}
	}
}

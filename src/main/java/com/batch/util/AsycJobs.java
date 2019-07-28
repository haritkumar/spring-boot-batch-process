package com.batch.util;

import java.util.logging.Logger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class AsycJobs {

	private static final Logger logger = Logger.getLogger(AsycJobs.class.getName());

	@Async
	public void processBatch(String n_name) {
		logger.info("Processing "+n_name);
	}
}

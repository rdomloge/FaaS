package com.example.faas.reactor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.Job;
import com.example.faas.reactor.rmq.Sender;

public class RuntimeExceptionHandlingRunnable implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuntimeExceptionHandlingRunnable.class);
	
	private Runnable target;
	
	private Job job;
	
	private Sender sender;
	

	public RuntimeExceptionHandlingRunnable(Runnable target, Job job, Sender sender) {
		this.target = target;
		this.job = job;
		this.sender = sender;
	}

	@Override
	public void run() {
		try {
			target.run();
		}
		catch(RuntimeException rex) {
			String msg = "Runtime exception "+rex.getClass().getSimpleName()+" for "
					+ job.getJobRequest().getFunctionName() + " during " + job.getJobId()
					+ " with " + job.getJobRequest().getCorrelationId();
			
			LOGGER.error(msg, rex);
			sender.sendError(job.getJobRequest(), msg, rex);
		}
	}

}

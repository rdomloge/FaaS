package com.example.faas.common;

import com.example.faas.dto.JobRequest;

public class Job {
	
	private String jobId;
	
	private JobRequest jobRequest;

	public Job(String jobId, JobRequest jobRequest) {
		super();
		this.jobId = jobId;
		this.jobRequest = jobRequest;
	}

	public String getJobId() {
		return jobId;
	}

	public JobRequest getJobRequest() {
		return jobRequest;
	}

}

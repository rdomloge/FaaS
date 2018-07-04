package com.example.faas.reactor.rmq;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.faas.common.JobRequest;
import com.example.faas.ex.FunctionException;
import com.example.faas.reactor.FunctionOrchestration;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Receiver {
	
	@Autowired
	private ObjectMapper mapper;
	
	private FunctionOrchestration orchestration;

	public void receive(String msg) throws IOException, FunctionException {
		JobRequest jobRequest = mapper.readValue(msg, JobRequest.class);
		orchestration.exec(jobRequest);
	}
}

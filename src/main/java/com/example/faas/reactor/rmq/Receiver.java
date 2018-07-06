package com.example.faas.reactor.rmq;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.faas.common.JobRequest;
import com.example.faas.ex.FunctionException;
import com.example.faas.reactor.FunctionOrchestration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Receiver {
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private FunctionOrchestration orchestration;

	public void receive(String msg) throws IOException, FunctionException {
		JobRequest jobRequest = mapper.readValue(msg, JobRequest.class);
		orchestration.exec(jobRequest);
	}
}

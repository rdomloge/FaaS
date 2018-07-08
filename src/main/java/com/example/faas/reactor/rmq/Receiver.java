package com.example.faas.reactor.rmq;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionException;
import com.example.faas.reactor.FunctionOrchestration;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Receiver {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private FunctionOrchestration orchestration;

	public void receive(String msg) throws IOException, FunctionException {
		LOGGER.debug("Received: {}", msg);
		JobRequest jobRequest = mapper.readValue(msg, JobRequest.class);
		orchestration.exec(jobRequest);
	}
}

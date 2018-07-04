package com.example.faas.reactor.rmq;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.faas.common.JobRequest;
import com.example.faas.ex.FunctionExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Sender {

	@Autowired
	private ObjectMapper mapper;
	
	
	public void send(JobRequest request, Object response) throws FunctionExecutionException {
		
		try {
			String valueAsString = mapper.writeValueAsString(response);
		} 
		catch (JsonProcessingException e) {
			throw new FunctionExecutionException("Could not convert to json: "+response, e);
		}
		
		String responseRoutingKey = request.getResponseRoutingKey();
		
		// write to RMQ, making sure to use the response routing key, so the message arrives to
		// the sender
		
	}
}

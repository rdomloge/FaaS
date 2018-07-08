package com.example.faas.reactor.rmq;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.faas.dto.JobRequest;
import com.example.faas.dto.JobResponse;
import com.example.faas.ex.FunctionExecutionException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Sender {

	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private RabbitTemplate template;
	
	
	public void send(JobRequest request, JobResponse response) throws FunctionExecutionException {
		
		String valueAsString;
		try {
			valueAsString = mapper.writeValueAsString(response);
		} 
		catch (JsonProcessingException e) {
			throw new FunctionExecutionException("Could not convert to json: "+response, e);
		}
//		
//		String responseRoutingKey = request.getResponseRoutingKey();
		
		// write to RMQ, making sure to use the response routing key, so the message arrives to
		// the sender
		template.send(request.getResponseRoutingKey(), 
				new Message(valueAsString.getBytes(), new MessageProperties()));
	}
}

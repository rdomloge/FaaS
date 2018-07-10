package com.example.faas.reactor.rmq;

import java.io.CharArrayWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.faas.dto.JobRequest;
import com.example.faas.dto.JobResponse;
import com.example.faas.dto.Outcome;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class Sender {

	private static final Logger LOGGER = LoggerFactory.getLogger(Sender.class);
	
	@Autowired
	private ObjectMapper mapper;
	
	@Autowired
	private RabbitTemplate template;
	
	
	public void send(JobRequest request, JobResponse response) {
		
		String valueAsString;
		try {
			valueAsString = mapper.writeValueAsString(response);
			
			// write to RMQ, making sure to use the response routing key, so the message arrives to
			// the sender
			template.send(request.getResponseRoutingKey(), 
					new Message(valueAsString.getBytes(), new MessageProperties()));
		} 
		catch (JsonProcessingException e) {
			sendError(request, "Could not convert to json: "+response, e);
		}
	}
	
	public void sendError(JobRequest request, String msg, Throwable e) {
		
		try {
			Map<String, String> details = new HashMap<>();
			details.put("error message", msg);
			details.put("stack trace", toString(e));
			
			JobResponse response = new JobResponse(request, details, Outcome.FAIL);
			
			String returnPayload = mapper.writeValueAsString(response);
			// write to RMQ, making sure to use the response routing key, so the message arrives to
			// the sender
			template.send(request.getResponseRoutingKey(), 
					new Message(returnPayload.getBytes(), new MessageProperties()));
		} 
		catch (JsonProcessingException ex) {
			LOGGER.error("Could not send error", ex);
		}
	}
	
	private String toString(Throwable e) {
		try(CharArrayWriter buf = new CharArrayWriter()) {
			try(PrintWriter pw = new PrintWriter(buf)) {
				e.printStackTrace(pw);
				return new String(buf.toCharArray());
			}
		}
	}
}

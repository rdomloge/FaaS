package com.example.faas.reactor;

import org.springframework.stereotype.Component;

@Component
public class JobIdGenerator {

	private long count;
	
	public String generate() {
		return "JOB-"+(++count);
	}
}

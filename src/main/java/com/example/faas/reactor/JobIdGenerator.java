package com.example.faas.reactor;

public class JobIdGenerator {

	private long count;
	
	public String generate() {
		return "JOB-"+(++count);
	}
}

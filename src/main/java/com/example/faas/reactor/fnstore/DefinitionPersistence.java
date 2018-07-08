package com.example.faas.reactor.fnstore;

import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.dto.JobRequest;

@Service
public class DefinitionPersistence {

	
	public FunctionDefinition load(JobRequest request) {
		String functionName = request.getFunctionName();
		// load function identified by functionName
		return null;
	}
}

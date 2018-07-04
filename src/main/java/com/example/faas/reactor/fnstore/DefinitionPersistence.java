package com.example.faas.reactor.fnstore;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.JobRequest;

public class DefinitionPersistence {

	
	public FunctionDefinition load(JobRequest request) {
		String functionName = request.getFunctionName();
		// load function identified by functionName
		return null;
	}
}

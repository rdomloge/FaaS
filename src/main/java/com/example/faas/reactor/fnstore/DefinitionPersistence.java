package com.example.faas.reactor.fnstore;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionPreparationException;

public interface DefinitionPersistence {

	FunctionDefinition load(JobRequest request) throws FunctionPreparationException;

}

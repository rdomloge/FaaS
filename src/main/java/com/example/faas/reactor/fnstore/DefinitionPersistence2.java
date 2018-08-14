package com.example.faas.reactor.fnstore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.LibResource;
import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionPreparationException;

public class DefinitionPersistence2 implements DefinitionPersistence  {

	@Override
	public FunctionDefinition load(JobRequest request) throws FunctionPreparationException {
		// TODO Auto-generated method stub
		return null;
	}

}

package com.example.faas.reactor.fnstore;

import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.LibResource;
import com.example.faas.dto.JobRequest;

@Service
public class DefinitionPersistence {

	
	public FunctionDefinition load(JobRequest request) {
		LibResource[] libs = {
			new FileBackedLibResource(
					"slf4j-simple-1.6.1.jar", 
					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-simple/1.6.1/slf4j-simple-1.6.1.jar"),
			new FileBackedLibResource(
					"slf4j-api-1.6.1.jar", 
					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar"),
			new FolderBackedLibResource(
					"classes", 
					"/Users/rdomloge/Documents/workspace/catholicon/target/classes/")};
		
		String functionName = request.getFunctionName();
		FunctionDefinition def = new FunctionDefinition(functionName, null, "Test", "", libs);
		// load function identified by functionName
		return def;
	}
}

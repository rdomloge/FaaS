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

@Service
public class DefinitionPersistence {

	
	public FunctionDefinition load(JobRequest request) throws FunctionPreparationException {
		LibResource[] libs = {
			new FileBackedLibResource(
					"slf4j-simple-1.6.1.jar", 
					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-simple/1.6.1/slf4j-simple-1.6.1.jar"),
			new FileBackedLibResource(
					"slf4j-api-1.6.1.jar", 
					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar"),
			new FileBackedLibResource(
					"catholicon.jar", 
					"/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/catholicon.jar")};
		
		String functionName = request.getFunctionName();
		
		Map<String, String> config = new HashMap<>();
		FunctionDefinition def = new FunctionDefinition(functionName, loadSrcFromFile(), "Test", "", 
				config, libs);
		// load function identified by functionName
		return def;
	}
	
	private static String loadSrcFromFile() throws FunctionPreparationException {
		StringBuilder sb = new StringBuilder();
		
		try(FileReader fr = new FileReader(new File("/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/Test.java"))) {
			char[] buf = new char[32];
			int read = 0;
			while((read = fr.read(buf)) != -1) {
				sb.append(buf, 0, read);
			}
		}
		catch(IOException ioex) {
			throw new FunctionPreparationException("Could not read source file", ioex);
		}
		
		return sb.toString();
	}
}

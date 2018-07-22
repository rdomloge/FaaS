package com.example.faas.reactor.fnstore;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.LibResource;
import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionPreparationException;

@Service
public class RamsayDefinitionPersistence implements DefinitionPersistence {

	public static final String FUNCTION_ROOT = "/Users/rdomloge/";
	public static final String REPO_ROOT = ".m2/repository/";
	
	
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
		
		String functionFileName = "/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/Test.java";
		
		String functionName = request.getFunctionName();
		FunctionDefinition def = new FunctionDefinition(functionName, loadSourceCode(functionFileName), "Test", "", libs);
		// load function identified by functionName

		return def;
	}

	private String loadSourceCode(String filename) throws FunctionPreparationException {
		StringBuilder sb = new StringBuilder();
		
		try(FileReader fr = new FileReader(new File(filename))) {
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

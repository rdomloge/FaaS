package com.example.faas.reactor.fnstore;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.dto.JobRequest;
import com.example.faas.ex.FunctionPreparationException;

public interface DefinitionPersistence {

	FunctionDefinition load(JobRequest request) throws FunctionPreparationException;

//	public FunctionDefinition load(JobRequest request) throws FunctionPreparationException {
//		LibResource[] libs = {
//			new FileBackedLibResource(
//					"slf4j-simple-1.6.1.jar", 
//					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-simple/1.6.1/slf4j-simple-1.6.1.jar"),
//			new FileBackedLibResource(
//					"slf4j-api-1.6.1.jar", 
//					"/Users/rdomloge/.m2/repository/org/slf4j/slf4j-api/1.6.1/slf4j-api-1.6.1.jar"),
//			new FileBackedLibResource(
//					"slf4j-api-1.6.1.jar", 
//					"/Users/rdomloge/.m2/repository/org/jsoup/jsoup/1.9.2/jsoup-1.9.2.jar"),
//			new FileBackedLibResource(
//					"catholicon.jar", 
//					"/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/catholicon.jar"),
//			new FileBackedLibResource(
//					"spring-core-4.3.6.RELEASE.jar", 
//					"/Users/rdomloge/.m2/repository/org/springframework/spring-core/4.3.6.RELEASE/spring-core-4.3.6.RELEASE.jar"),
//			new FileBackedLibResource(
//					"httpclient-4.5.2.jar", 
//					"/Users/rdomloge/.m2/repository/org/apache/httpcomponents/httpclient/4.5.2/httpclient-4.5.2.jar"),
//			new FileBackedLibResource(
//					"httpcore-4.4.6.jar", 
//					"/Users/rdomloge/.m2/repository/org/apache/httpcomponents/httpcore/4.4.6/httpcore-4.4.6.jar"),
//			new FileBackedLibResource(
//					"commons-logging-1.1.1.jar", 
//					"/Users/rdomloge/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar"),
//			new FileBackedLibResource(
//					"javax.servlet-api-3.1.0.jar", 
//					"/Users/rdomloge/.m2/repository/javax/servlet/javax.servlet-api/3.1.0/javax.servlet-api-3.1.0.jar")
//			};
//		
//		String functionName = request.getFunctionName();
//		
//		Map<String, String> config = new HashMap<>();
//		config.put("BASE_URL", "http://bdbl.org.uk");
//		FunctionDefinition def = new FunctionDefinition(
//				functionName, loadSrcFromFile(), "CatholiconRecentMatchesSpiderFunction", "catholicon", 
//				config, libs);
//		
//		return def;
//	}
//	
//	private static String loadSrcFromFile() throws FunctionPreparationException {
//		StringBuilder sb = new StringBuilder();
//		
//		try(FileReader fr = new FileReader(new File("/Users/rdomloge/Documents/workspace/FaaS/src/main/test-functions/CatholiconRecentMatchesSpiderFunction.java"))) {
//			char[] buf = new char[32];
//			int read = 0;
//			while((read = fr.read(buf)) != -1) {
//				sb.append(buf, 0, read);
//			}
//		}
//		catch(IOException ioex) {
//			throw new FunctionPreparationException("Could not read source file", ioex);
//		}
//		
//		return sb.toString();
//	}
}

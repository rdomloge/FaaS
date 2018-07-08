package com.example.faas.reactor;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.dto.JobRequest;
import com.example.faas.dto.JobResponse;
import com.example.faas.ex.FunctionException;
import com.example.faas.reactor.exec.Executor;
import com.example.faas.reactor.fnloader.FunctionLoader;
import com.example.faas.reactor.fnstore.DefinitionPersistence;
import com.example.faas.reactor.rmq.Sender;
import com.example.faas.reactor.workspace.WorkspaceManager;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FunctionOrchestration {
	
	@Autowired
	private DefinitionPersistence persistence;
	
	@Autowired
	private JobIdGenerator idGenerator;
	
	@Autowired
	private FunctionLoader functionLoader;
	
	@Autowired
	private WorkspaceManager workspaceMgr;
	
	@Autowired
	private Executor executor;
	
	@Autowired
	private Sender sender;
	
	@Autowired
	private ObjectMapper mapper;

	public void exec(JobRequest request) throws FunctionException {
		String jobId = idGenerator.generate();
		Job job = new Job(jobId, request);
if(true) {
	Object result = "demo return value";
	String json;
	try {
		json = mapper.writeValueAsString(result);
	} catch (JsonProcessingException e) {
		throw new FunctionException("Could not create JSON");
	}
	sender.send(request, new JobResponse(request, json));
	return;
}
		FunctionDefinition functionDefinition = persistence.load(request);
		WorkspaceResourcesDescriptor workspaceResourcesDescriptor = 
				workspaceMgr.prepare(job, functionDefinition);
		
		/* we might not want to close the execution resource here...
		 * might want to keep it in a cache for a while */
		try(ExecutionResource executionResource = functionLoader.load(workspaceResourcesDescriptor)) {
			Object result = executor.execute(executionResource);
			String json = mapper.writeValueAsString(result);
			sender.send(request, new JobResponse(request, json));
		} 
		catch (IOException e) {
			// maybe we should log something here
			// but all that happened was that we failed to cleanup the execution resource
			// this typically means closing a URLClassloader
		}
	}
}

package com.example.faas.reactor;

import org.springframework.beans.factory.annotation.Autowired;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.JobRequest;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionException;
import com.example.faas.reactor.exec.Executor;
import com.example.faas.reactor.fnloader.FunctionLoader;
import com.example.faas.reactor.fnstore.DefinitionPersistence;
import com.example.faas.reactor.rmq.Sender;
import com.example.faas.reactor.workspace.WorkspaceManager;

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

	public void exec(JobRequest request) throws FunctionException {
		String jobId = idGenerator.generate();
		Job job = new Job(jobId, request);
		FunctionDefinition functionDefinition = persistence.load(request);
		WorkspaceResourcesDescriptor workspaceResourcesDescriptor = 
				workspaceMgr.prepare(job, functionDefinition);
		ExecutionResource executionResource = functionLoader.load(workspaceResourcesDescriptor);
		Object result = executor.execute(executionResource);
		sender.send(request, result);
	}
}

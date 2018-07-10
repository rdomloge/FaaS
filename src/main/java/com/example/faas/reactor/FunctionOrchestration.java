package com.example.faas.reactor;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.dto.JobRequest;
import com.example.faas.dto.JobResponse;
import com.example.faas.dto.Outcome;
import com.example.faas.ex.FunctionException;
import com.example.faas.ex.FunctionPreparationException;
import com.example.faas.reactor.exec.Executor;
import com.example.faas.reactor.fnloader.FunctionLoader;
import com.example.faas.reactor.fnstore.DefinitionPersistence;
import com.example.faas.reactor.rmq.Sender;
import com.example.faas.reactor.workspace.WorkspaceManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FunctionOrchestration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionOrchestration.class);
	
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
	
	private ExecutorService exec;
	
	@PostConstruct
	public void startup() {
		exec = Executors.newWorkStealingPool();
	}
	
	@PreDestroy
	public void shutdown() {
		exec.shutdownNow();
	}

	public void orchestrate(JobRequest request) throws FunctionException {
		String jobId = idGenerator.generate();
		Job job = new Job(jobId, request);
		
		Runnable r = new Runnable() {
			@Override
			public void run() {
				
				FunctionDefinition functionDefinition = persistence.load(request);
				try {
					WorkspaceResourcesDescriptor workspaceResourcesDescriptor = 
							workspaceMgr.prepare(job, functionDefinition);
					
					/* we might not want to close the execution resource here...
					 * might want to keep it in a cache for a while */
					try(ExecutionResource executionResource = functionLoader.load(workspaceResourcesDescriptor)) {
						Object result = executor.execute(executionResource);
						sender.send(request, new JobResponse(request, result, Outcome.SUCCESS));
					} 
					catch (IOException e) {
						// maybe we should log something here
						// but all that happened was that we failed to cleanup the execution resource
						// this typically means closing a URLClassloader
						LOGGER.error("Error cleaning up the execution resource", e);
					} 
					catch (FunctionException e) {
						sender.sendError(request, "Could not execute", e);
					}
				}
				catch(FunctionPreparationException fpex) {
					sender.sendError(request, "Could not prepare", fpex);
				}
			}
		};
		exec.submit(new RuntimeExceptionHandlingRunnable(r, job, sender));
	}
}

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
import org.springframework.util.StopWatch;

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
import com.example.faas.reactor.fnstore.DatabaseDefinitionPersistence;
import com.example.faas.reactor.rmq.Sender;
import com.example.faas.reactor.workspace.WorkspaceManager;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class FunctionOrchestration {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionOrchestration.class);
	
	@Autowired
	private DatabaseDefinitionPersistence persistence;
	
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
				StopWatch stopWatch = new StopWatch(
						request.getFunctionName() + 
						" -- " + 
						jobId +
						" -- "+
						request.getCorrelationId());
				stopWatch.start("Load "+request.getFunctionName()+" function definition ["+jobId+"]");
				try {
					FunctionDefinition functionDefinition = persistence.load(request);
					stopWatch.stop();
					stopWatch.start("Prepare "+request.getFunctionName()+" function files ["+jobId+"]");
					WorkspaceResourcesDescriptor workspaceResourcesDescriptor = 
							workspaceMgr.prepare(job, functionDefinition);
					
					stopWatch.stop();
					stopWatch.start("Instantiate "+request.getFunctionName()+" function ["+jobId+"]");
					
					/* we might not want to close the execution resource here...
					 * might want to keep it in a cache for a while */
					try(ExecutionResource executionResource = functionLoader.load(workspaceResourcesDescriptor)) {
						stopWatch.stop();
						stopWatch.start("Execute "+request.getFunctionName()+" function ["+jobId+"]");
						
						Object result = executor.execute(executionResource);
						stopWatch.stop();
						stopWatch.start("Send response for "+request.getFunctionName()+" function ["+jobId+"]");
						
						sender.send(request, new JobResponse(request, result, Outcome.SUCCESS));
					} 
					catch (IOException e) {
						// maybe we should log something here
						// but all that happened was that we failed to cleanup the execution resource
						// this typically means closing a URLClassloader
						LOGGER.error("Error cleaning up the execution resource", e);
						// no need to send error here - this is only called after the response is sent
					} 
					catch(NoClassDefFoundError err) {
						LOGGER.error("Incomplete classpath", err);
						sender.sendError(request, "Could not execute", err);
					}
					catch (FunctionException e) {
						LOGGER.error("Function execution failed", e);
						sender.sendError(request, "Could not execute", e);
					}
				}
				catch(FunctionPreparationException fpex) {
					sender.sendError(request, "Could not prepare", fpex);
				}
				finally {
					stopWatch.stop();
					LOGGER.info("Function complete\n"+stopWatch.prettyPrint());	
				}
				
			}
		};
		exec.submit(new RuntimeExceptionHandlingRunnable(r, job, sender));
	}
}

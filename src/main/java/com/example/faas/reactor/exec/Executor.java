package com.example.faas.reactor.exec;

import org.springframework.stereotype.Service;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.dto.JobRequest;

@Service
public class Executor<V> {
	
	public V execute(ExecutionResource<V> resource) {
		
		// probably best to submit this to a thread pool, such as a Scheduled Executor
		
		Function<V> function = resource.getFunction();
		JobRequest jobRequest = resource.getWorkspaceResourcesDescriptor().getJob().getJobRequest();
		function.setRequest(jobRequest);
		return function.call();
	}

}

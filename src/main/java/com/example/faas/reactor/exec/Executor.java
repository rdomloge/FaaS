package com.example.faas.reactor.exec;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.common.JobRequest;

public class Executor<V> {
	
	public V execute(ExecutionResource<V> resource) {
		
		// probably best to submit this to a thread pool, such as a Scheduled Executor
		
		Function<V> function = resource.getFunction();
		JobRequest jobRequest = resource.getWorkspaceResourcesDescriptor().getJob().getJobRequest();
		function.setRequest(jobRequest);
		return function.call();
	}

}

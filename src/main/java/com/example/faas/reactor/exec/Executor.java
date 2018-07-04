package com.example.faas.reactor.exec;

import java.io.IOException;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;

public class Executor<V> {
	
	public V execute(ExecutionResource<V> resource) {
		
		// probably best to submit this to a thread pool, such as a Scheduled Executor
		
		Function<V> function = resource.getFunction();
		try {
			return function.call();
		}
		finally {
			try {
				resource.close();
			} 
			catch (IOException e) {
				// log it, if you like
			}	
		}
	}

}

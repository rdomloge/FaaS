package com.example.faas.common;

import java.io.Closeable;

public abstract class ExecutionResource<V> implements Closeable {
	
	private Function<V> function;
	
	private WorkspaceResourcesDescriptor preparedDescriptor;

	public ExecutionResource(Function<V> function, WorkspaceResourcesDescriptor preparedDescriptor) {
		super();
		this.function = function;
		this.preparedDescriptor = preparedDescriptor;
	}

	public Function<V> getFunction() {
		return function;
	}

	public WorkspaceResourcesDescriptor getPreparedDescriptor() {
		return preparedDescriptor;
	}
	
}

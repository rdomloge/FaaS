package com.example.faas.common;

import java.io.Closeable;
import java.io.IOException;

public class ExecutionResource<V> implements Closeable {
	
	private Function<V> function;
	
	private WorkspaceResourcesDescriptor workspaceResourcesDescriptor;

	public ExecutionResource(Function<V> function, WorkspaceResourcesDescriptor workspaceResourcesDescriptor) {
		this.function = function;
		this.workspaceResourcesDescriptor = workspaceResourcesDescriptor;
	}

	public Function<V> getFunction() {
		return function;
	}

	
	public WorkspaceResourcesDescriptor getWorkspaceResourcesDescriptor() {
		return workspaceResourcesDescriptor;
	}

	@Override
	public void close() throws IOException {
		workspaceResourcesDescriptor.close();
	}
	
}

package com.example.faas.reactor.fnloader;

import java.io.IOException;
import java.net.URLClassLoader;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.common.Job;
import com.example.faas.common.WorkspaceResourcesDescriptor;

public class ClassloaderExecutionResource<V> extends ExecutionResource<V> {

	private URLClassLoader classLoader;

	public ClassloaderExecutionResource(Function<V> function, 
			WorkspaceResourcesDescriptor workspaceResourcesDescriptor, URLClassLoader classLoader) {
		super(function, workspaceResourcesDescriptor);
		this.classLoader = classLoader;
	}

	@Override
	public void close() throws IOException {
		classLoader.close();
	}

}

package com.example.faas.reactor.fnloader;

import java.io.IOException;
import java.net.URLClassLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.common.WorkspaceResourcesDescriptor;

public class ClassloaderExecutionResource<V> extends ExecutionResource<V> {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ClassloaderExecutionResource.class);

	private URLClassLoader classLoader;

	public ClassloaderExecutionResource(Function<V> function, 
			WorkspaceResourcesDescriptor workspaceResourcesDescriptor, URLClassLoader classLoader) {
		super(function, workspaceResourcesDescriptor);
		this.classLoader = classLoader;
	}

	@Override
	public void close() throws IOException {
		super.close();
		classLoader.close();
		LOGGER.debug("Classloader closed for {} for fn {}", 
				getWorkspaceResourcesDescriptor().getJob().getJobId(),
				getWorkspaceResourcesDescriptor().getFunctionDefinition().getFunctionUniqueName());
	}

}

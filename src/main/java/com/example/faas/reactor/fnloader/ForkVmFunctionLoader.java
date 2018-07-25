package com.example.faas.reactor.fnloader;

import org.springframework.stereotype.Component;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionException;

@Component
public class ForkVmFunctionLoader implements FunctionLoader {

	public ExecutionResource load(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) 
			throws FunctionException {
		Function function = new ForkVmFunction(workspaceResourcesDescriptor);
		return new ExecutionResource(function, workspaceResourcesDescriptor);
	}
}

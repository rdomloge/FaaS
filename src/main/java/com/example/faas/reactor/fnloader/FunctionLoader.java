package com.example.faas.reactor.fnloader;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionException;

public interface FunctionLoader {

	ExecutionResource load(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) 
			throws FunctionException;
}
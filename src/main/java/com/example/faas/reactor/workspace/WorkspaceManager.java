package com.example.faas.reactor.workspace;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.WorkspaceResourcesDescriptor;

public class WorkspaceManager {

	public WorkspaceResourcesDescriptor prepare(Job job, FunctionDefinition functionDefinition) {
		
		String functionUniqueName = functionDefinition.getFunctionUniqueName();
		String jobId = job.getJobId();
		// create a job folder in some configured root folder, named by the job id and function name
		
		// copy in the FaaS libs to <ROOT>/<JOB>/faasLibs
		
		// copy in the function's libs to <ROOT>/<JOB>/lib
		
		// compile the source to <ROOT>/<JOB>/bin
		
		
		return null;
	}
	
	public void cleanup(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) {
		// delete the folder recursively
	}
}

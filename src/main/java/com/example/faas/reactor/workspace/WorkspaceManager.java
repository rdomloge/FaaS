package com.example.faas.reactor.workspace;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.LibResource;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionPreparationException;

@Service
public class WorkspaceManager {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceManager.class);
	
	private File root;
	
	public WorkspaceResourcesDescriptor prepare(Job job, FunctionDefinition functionDefinition) throws FunctionPreparationException {
		
		String functionUniqueName = functionDefinition.getFunctionUniqueName();
		String jobId = job.getJobId();
		// create a job folder in some configured root folder, named by the job id and function name
		
		// copy in the FaaS libs to <ROOT>/<JOB>/faasLibs
		
		// copy in the function's libs to <ROOT>/<JOB>/lib
		
		// compile the source to <ROOT>/<JOB>/bin
		File workspace = createWorkspaceFolder(functionDefinition, jobId);
		File libFolder = checkOrMakeFolder(new File(workspace, "lib"));
		copyLibs(libFolder, functionDefinition.getLibs());
		File compiledBinFolder = checkOrMakeFolder(new File(workspace, "classes"));
		File[] faasLibs = new File[] {};
		
		return new WorkspaceResourcesDescriptor(
				functionDefinition, 
				job, 
				workspace, 
				libFolder, 
				compiledBinFolder, 
				faasLibs);
	}
	
	private void copyLibs(File libFolder, LibResource[] libs) throws FunctionPreparationException {
		try {
			for (LibResource resource : libs) {
				resource.copyResourcesTo(libFolder);
			}
		}
		catch(IOException ioex) {
			LOGGER.error("Error copying lib resource", ioex);
			throw new FunctionPreparationException("Error copying lib resource", ioex);
		}
	}
	
	private File createWorkspaceFolder(FunctionDefinition def, String jobId) throws FunctionPreparationException  {
		File definitionFolder = new File(root, def.getFunctionUniqueName());
		File jobFolder = new File(definitionFolder, jobId);
		
		try {
			checkOrMakeFolder(definitionFolder);
			checkOrMakeFolder(jobFolder);
			return jobFolder;
		}
		catch(FunctionPreparationException fpex) {
			throw new FunctionPreparationException("Could not create workspace folder", fpex);
		}
		
	}
	
	public void cleanupWorkspaceFolder(File f) throws IOException {
		if(f.isFile()) {
			if( ! f.delete()) throw new IOException("Could not delete "+f);
		}
		else if(f.isDirectory()) {
			File[] children = f.listFiles();
			for (File child : children) {
				cleanupWorkspaceFolder(child);
			}
			f.delete();
		}
		else throw new IOException("What the hell is "+f);
	}

	private File checkOrMakeFolder(File folder) throws FunctionPreparationException {
		
		if(folder.exists() && folder.isDirectory()) return folder;
		
		if( ! folder.mkdirs()) {
			throw new FunctionPreparationException("Could not make "+folder.getAbsolutePath());
		}
		
		return folder;
	}

	public void cleanup(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) {
		// delete the folder recursively
	}
}

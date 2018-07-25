package com.example.faas.common;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class WorkspaceResourcesDescriptor implements Closeable {
	
	private FunctionDefinition functionDefinition;
	
	private Job job;
	
	private File workspace;
	
	private File libFolder;
	
	private File compiledBinFolder;

	private File[] faasLibs;

	public WorkspaceResourcesDescriptor(FunctionDefinition functionDefinition, Job job, File workspace, 
			File libFolder, File compiledBinFolder, File[] faasLibs) {
		
		this.functionDefinition = functionDefinition;
		this.job = job;
		this.workspace = workspace;
		this.libFolder = libFolder;
		this.compiledBinFolder = compiledBinFolder;
		this.faasLibs = faasLibs;
	}

	public FunctionDefinition getFunctionDefinition() {
		return functionDefinition;
	}

	public Job getJob() {
		return job;
	}

	public File getWorkspace() {
		return workspace;
	}

	public File getLibFolder() {
		return libFolder;
	}

	public File getCompiledBinFolder() {
		return compiledBinFolder;
	}

	public File[] getFaasLibs() {
		return faasLibs;
	}

	@Override
	public void close() throws IOException {
		// delete loads of stuff here
	}
	
	
}

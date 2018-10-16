package com.example.faas.reactor.fnloader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.AbstractFunction;
import com.example.faas.common.LibResource;
import com.example.faas.common.WorkspaceResourcesDescriptor;

public class ForkVmFunction extends AbstractFunction {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ForkVmFunction.class);
	
	private WorkspaceResourcesDescriptor wrd;

	
	public ForkVmFunction(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) {
		this.wrd = workspaceResourcesDescriptor;
	}

	@Override
	public Object call() {
		String separator = System.getProperty("file.separator");
		String classpath = buildClassPath();
	    String path = System.getProperty("java.home")
	                + separator + "bin" + separator + "java";
	    
	    String fullyQualifiedClassName = wrd.getFunctionDefinition()
	    		.getFullyQualifiedClassName();
	    
	    LOGGER.info("Executing {} with classpath {} for class {}", path, classpath, fullyQualifiedClassName);
	    
	    Properties config = wrd.getFunctionDefinition().getConfig();
	    File configFile = new File(wrd.getWorkspace(), "config.txt");
	    try {
			config.store(new FileOutputStream(configFile), "Config for "+wrd.getFunctionDefinition().getFunctionUniqueName());
		} 
	    catch (IOException e) {
	    	throw new RuntimeException("Could not write config to file", e);
		}
	    
	    Properties params = wrd.getJob().getJobRequest().getParams();
	    File paramsFile = new File(wrd.getWorkspace(), "params-"+wrd.getJob().getJobId()+".txt");
	    try {
			params.store(new FileOutputStream(paramsFile), "No comments");
		} 
	    catch (IOException e) {
	    	throw new RuntimeException("Could not write params to file", e);
		}
	    
	    ProcessBuilder processBuilder = new ProcessBuilder(
	    		path, 
	    		"-cp", 
                classpath,
                "com.example.faas.vm.config.ApplicationMain",
                fullyQualifiedClassName,
                wrd.getJob().getJobId(),
                wrd.getWorkspace().getAbsolutePath());
	    
	    List<String> command = processBuilder.command();
	    LOGGER.debug("Command: {}", command);
	    try {
		    Process process = processBuilder.start();
		    
		    ProcessStreamCapture capture = new ProcessStreamCapture(process, wrd.getWorkspace(), wrd.getJob());
		    capture.startCapture();
		    
		    LOGGER.debug("Process started");
		    process.waitFor();
		    
		    LOGGER.debug("Process ended");
		    capture.cleanUp();
	    }
	    catch(Exception e) {
	    	LOGGER.error("Error running VM", e);
	    }
	    
		return null;
	}

	private String buildClassPath() {
		LOGGER.debug("Building classpath: ");
		String separator = System.getProperty("path.separator");
		StringBuilder sb = new StringBuilder();
		File[] faasLibs = wrd.getFaasLibs();
		for (File lib : faasLibs) {
			if(sb.length() > 0) sb.append(separator);
			if( ! lib.exists()) throw new IllegalStateException(lib.getAbsolutePath());
			sb.append(lib.getAbsolutePath());
			LOGGER.debug("> {}", lib.getAbsolutePath());
		}
		
		if(sb.length() > 0) sb.append(separator);
		if( ! wrd.getCompiledBinFolder().exists()) throw new IllegalStateException(wrd.getCompiledBinFolder().getAbsolutePath());
		sb.append(wrd.getCompiledBinFolder().getAbsolutePath());
		LOGGER.debug("> {}", wrd.getCompiledBinFolder().getAbsolutePath());
		
		File libFolder = wrd.getLibFolder();
		LibResource[] libs = wrd.getFunctionDefinition().getLibs();
		for (LibResource lib : libs) {
			if(sb.length() > 0) sb.append(separator);
			File libCopy = new File(libFolder, lib.getLibName());
			if( ! libCopy.exists()) throw new IllegalStateException(libCopy.getAbsolutePath());
			sb.append(libCopy.getAbsolutePath());
			LOGGER.debug("> {}", libCopy.getAbsolutePath());
		}
		
		return sb.toString();
	}
}

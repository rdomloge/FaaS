package com.example.faas.reactor.fnloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.example.faas.common.ExecutionResource;
import com.example.faas.common.Function;
import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.LibResource;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionException;
import com.example.faas.ex.FunctionPreparationException;


public class FunctionLoaderImpl implements FunctionLoader {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FunctionLoaderImpl.class);
	
	
	public ExecutionResource load(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) 
			throws FunctionException {
		
		URL[] classPathUrls = buildClassPathUrls(workspaceResourcesDescriptor);
		try {
			URLClassLoader cl = new URLClassLoader(classPathUrls, Function.class.getClassLoader());
			Class<?> loaded = cl.loadClass(
					workspaceResourcesDescriptor.getFunctionDefinition().getFullyQualifiedClassName());
			if( ! Function.class.isAssignableFrom(loaded)) 
				throw new FunctionPreparationException(String.format("%s is not a function", 
						workspaceResourcesDescriptor.getFunctionDefinition().getFunctionClassName()));
			Object instance = loaded.newInstance();
			Function<?> f = (Function<?>) instance;
			f.setStaticConfig(workspaceResourcesDescriptor.getFunctionDefinition().getConfig());
			LOGGER.debug("Function loaded");
			return new ClassloaderExecutionResource(f, workspaceResourcesDescriptor, cl);
		}
		catch(ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			LOGGER.info("Built classpath URLs [{}]", Arrays.toString(classPathUrls));
			LOGGER.error("Could not create FN instance ", e);
			throw new FunctionPreparationException(e);
		}
	}
	
	private URL[] buildClassPathUrls(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) 
			throws FunctionPreparationException {
		
		FunctionDefinition functionDefinition = workspaceResourcesDescriptor.getFunctionDefinition();
		List<URL> urls = new LinkedList<>();
		try {
			LibResource[] libs = functionDefinition.getLibs();
			for (int i = 0; i < libs.length; i++) {
				File file = new File(workspaceResourcesDescriptor.getLibFolder(), libs[i].getLibName());
				if( ! file.exists()) throw new FunctionPreparationException("Lib missing: "+file);
				urls.add(file.toURI().toURL());
			}
			
			File compiledBinFolder = workspaceResourcesDescriptor.getCompiledBinFolder();
			if( ! compiledBinFolder.exists()) 
				throw new FunctionPreparationException("compiledBinFolder missing: "+compiledBinFolder);
			urls.add(compiledBinFolder.toURI().toURL());
			
			File[] faasLibs = workspaceResourcesDescriptor.getFaasLibs();
			for (File faasLib : faasLibs) {
				if( ! faasLib.exists()) throw new FunctionPreparationException("FaaS Lib missing: "+faasLib);
				urls.add(faasLib.toURI().toURL());
			}
		}
		catch(MalformedURLException ex) {
			throw new FunctionPreparationException("Could not use library", ex);
		}
		return urls.toArray(new URL[urls.size()]);
	}
	
}

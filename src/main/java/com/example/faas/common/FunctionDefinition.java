package com.example.faas.common;

import java.util.Map;

public class FunctionDefinition {
	
	private String functionUniqueName;
	
	private String source;
	
	private String functionClassName;
	
	private String packageName;
	
	private Map<String, String> config;
	
	private LibResource[] libs;

	

	public FunctionDefinition(String functionUniqueName, String source, String functionClassName, 
			String packageName, Map<String, String> config, LibResource[] libs) {
		
		this.functionUniqueName = functionUniqueName;
		this.source = source;
		this.functionClassName = functionClassName;
		this.packageName = packageName;
		this.config = config;
		this.libs = libs;
	}

	public String getFunctionUniqueName() {
		return functionUniqueName;
	}

	public String getSource() {
		return source;
	}

	public String getFunctionClassName() {
		return functionClassName;
	}

	public String getPackageName() {
		return packageName;
	}

	public LibResource[] getLibs() {
		return libs;
	}

	public Map<String, String> getConfig() {
		return config;
	}
}

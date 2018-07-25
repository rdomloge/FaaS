package com.example.faas.common;

import java.util.Map;

public class FunctionDefinition {
	
	private String functionUniqueName;
	
	private String sourceCode;
	
	private String functionClassName;
	
	private String packageName;
	
	private Map<String, String> config;
	
	private LibResource[] libs;

	public FunctionDefinition(String functionUniqueName, String sourceCode, String functionClassName, 
			String packageName, Map<String, String> config, LibResource[] libs) {
		
		this.functionUniqueName = functionUniqueName;
		this.sourceCode = sourceCode;
		this.functionClassName = functionClassName;
		this.packageName = packageName;
		this.config = config;
		this.libs = libs;
	}

	public String getFunctionUniqueName() {
		return functionUniqueName;
	}

	public String getSourceCode() {
		return sourceCode;
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
	
	public String getFullyQualifiedClassName() {
		StringBuilder builder = new StringBuilder();
		if(null != packageName && packageName.trim().length() > 0) {
			builder.append(packageName);
			builder.append('.');
		}
		builder.append(this.functionClassName);
		return builder.toString();
	}
}

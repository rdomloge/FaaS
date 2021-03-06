package com.example.faas.common;

import java.util.Properties;

public class FunctionDefinition {
	
	private String functionUniqueName;
	
	private String sourceCode;
	
	private String functionClassName;
	
	private String packageName;
	
	private Properties config;
	
	private LibResource[] libs;

	public FunctionDefinition(String functionUniqueName, String sourceCode, String functionClassName, 
			Properties config, LibResource[] libs) {
		
		this.functionUniqueName = functionUniqueName;
		this.sourceCode = sourceCode;
//		this.functionClassName = functionClassName;
//		this.packageName = packageName;
		splitPackageNameAndClassSimpleName(functionClassName);
		this.config = config; // Populated from DB
		this.libs = libs;
	}
	
	private void splitPackageNameAndClassSimpleName(String fullyQualifiedClassName) {
		if(fullyQualifiedClassName.contains(".")) {
			this.packageName = 
					fullyQualifiedClassName.substring(0, fullyQualifiedClassName.lastIndexOf("."));
			this.functionClassName = 
					fullyQualifiedClassName.substring(fullyQualifiedClassName.lastIndexOf(".")+1);
		}
		else {
			functionClassName = fullyQualifiedClassName;
			packageName = "";
		}
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

	public String[] getPackageName() {
		if(packageName.trim().length() < 1) return new String[] { };
		if(packageName.contains(".")) return packageName.split(".");
		return new String[] { packageName };
	}

	public LibResource[] getLibs() {
		return libs;
	}

	public Properties getConfig() {
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

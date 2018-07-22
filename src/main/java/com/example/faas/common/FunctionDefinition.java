package com.example.faas.common;

public class FunctionDefinition {
	
	private String functionUniqueName;
	
	private String sourceCode;
	
	private String functionClassName;
	
	private String packageName;
	
	private LibResource[] libs;

	public FunctionDefinition(String functionUniqueName, String sourceCode, String functionClassName, 
			String packageName, LibResource[] libs) {
		this.functionUniqueName = functionUniqueName;
		this.sourceCode = sourceCode;
		this.functionClassName = functionClassName;
		this.packageName = packageName;
		this.libs = libs;
	}

	public String getFunctionUniqueName() {
		return functionUniqueName;
	}

	public String getSource() {
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
}

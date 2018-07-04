package com.example.faas.common;

public class FunctionDefinition {
	
	private String functionUniqueName;
	
	private String source;
	
	private String functionClassName;
	
	private String packageName;
	
	private String[] libs;

	public FunctionDefinition(String functionUniqueName, String source, String functionClassName, String packageName,
			String[] libs) {
		super();
		this.functionUniqueName = functionUniqueName;
		this.source = source;
		this.functionClassName = functionClassName;
		this.packageName = packageName;
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

	public String[] getLibs() {
		return libs;
	}

	

}

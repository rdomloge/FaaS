package com.example.faas.common;

import java.util.Map;

public class JobRequest {

	private String functionName;
	
	private Map<String, String> params;
	
	private String responseRoutingKey;

	public JobRequest(String functionName, Map<String, String> params, String responseRoutingKey) {
		this.functionName = functionName;
		this.params = params;
		this.responseRoutingKey = responseRoutingKey;
	}

	public String getFunctionName() {
		return functionName;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getResponseRoutingKey() {
		return responseRoutingKey;
	}

	
}

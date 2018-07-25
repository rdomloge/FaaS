package com.example.faas.sqlite.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Functions {

	@JsonProperty("functions")
	private List<Function> functions;

	public Functions() {
		this.functions = new ArrayList<>();
	}
	
	public List<Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Function> functions) {
		this.functions = functions;
	}

}

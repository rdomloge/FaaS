package com.example.faas.common;

import java.io.File;
import java.io.IOException;

public abstract class LibResource {

	private String libName;
	
	
	public LibResource(String libName) {
		super();
		this.libName = libName;
	}

	
	public abstract void copyResourcesTo(File folder) throws IOException;

	
	public String getLibName() {
		return libName;
	}
	
}

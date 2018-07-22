package com.example.faas.reactor.fnstore;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.LibResource;

public class DatabaseBackedLibResource extends LibResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseBackedLibResource.class);
	
	protected String libPath;
	protected byte[] file;

	public DatabaseBackedLibResource(String libPath, String libName, byte[] file) {
		super(libName);
		this.libPath = libPath;
		this.file = file;
	}

	@Override
	public void copyResourcesTo(File folder) throws IOException {
		copyBytes(file, new File(folder, getLibName()));
	}

	protected void copyBytes(byte[] source, File dst) throws IOException {
		LOGGER.debug("Copying source file to {}", dst.getAbsolutePath());
		
		try (ByteArrayInputStream bais = new ByteArrayInputStream(source)) {
			try(FileOutputStream fos = new FileOutputStream(dst)) {
				byte [] buf = new byte[32];
				int read = 0;
				while( (read = bais.read(buf)) != -1) {
					fos.write(buf, 0, read);
				}
			}
		}
	}
}

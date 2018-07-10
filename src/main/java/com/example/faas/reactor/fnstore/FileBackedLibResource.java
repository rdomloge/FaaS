package com.example.faas.reactor.fnstore;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.LibResource;

public class FileBackedLibResource extends LibResource {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileBackedLibResource.class);
	
	protected String libPath;

	public FileBackedLibResource(String libName, String libPath) {
		super(libName);
		this.libPath = libPath;
	}

	@Override
	public void copyResourcesTo(File folder) throws IOException {
		copyBytes(new File(libPath), new File(folder, getLibName()));
	}

	protected void copyBytes(File src, File dst) throws IOException {
		LOGGER.debug("Copying {} to {}", src.getAbsolutePath(), dst.getAbsolutePath());
		try(FileInputStream fis = new FileInputStream(src)) {
			try(FileOutputStream fos = new FileOutputStream(dst)) {
				byte [] buf = new byte[32];
				int read = 0;
				while( (read = fis.read(buf)) != -1) {
					fos.write(buf, 0, read);
				}
			}
		}
	}
}

package com.example.faas.reactor.fnstore;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FolderBackedLibResource extends FileBackedLibResource {

	private static final Logger LOGGER = LoggerFactory.getLogger(FolderBackedLibResource.class);
	
	public FolderBackedLibResource(String libName, String libPath) {
		super(libName, libPath);
	}

	@Override
	public void copyResourcesTo(File dstFolder) throws IOException {
		File srcFolder = new File(libPath);
		copy(srcFolder, dstFolder);
	}

	private void copy(File src, File dst) throws IOException {
		if(src.isFile()) {
			copyFile(src, dst);
		}
		else if(src.isDirectory()) {
			File[] files = src.listFiles();
			File childFolder = new File(dst, src.getName());
			if( ! childFolder.mkdir()) throw new IOException("Could not create "+childFolder.getAbsolutePath());
			for (File child : files) {
				copy(child, childFolder);
			}
		}
		else {
			LOGGER.warn("Ignoring "+src.getAbsolutePath());
		}
	}
	
	private void copyFile(File src, File folder) throws IOException {
		copyBytes(src, new File(folder, src.getName()));
	}
}

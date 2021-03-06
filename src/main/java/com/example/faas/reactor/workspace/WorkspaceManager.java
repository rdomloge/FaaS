package com.example.faas.reactor.workspace;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.tools.JavaCompiler;
import javax.tools.StandardJavaFileManager;
import javax.tools.StandardLocation;
import javax.tools.ToolProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.example.faas.common.FunctionDefinition;
import com.example.faas.common.Job;
import com.example.faas.common.LibResource;
import com.example.faas.common.WorkspaceResourcesDescriptor;
import com.example.faas.ex.FunctionPreparationException;

@Service
@PropertySource("classpath:application.properties")
public class WorkspaceManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(WorkspaceManager.class);

	@Value("${target.root.path}")
	private String rootPath;

	@Value("${api.lib.path}")
	private String apiLibPath;

//	@Value("${dto.jar}")
//	public String dtoJar;
	
	@Value("${fork.vm.lib.path}")
	private String forkVmLibPath;
	
	@Value("${jackson.lib.paths}")
	private String[] jacksonLibs;
	
	private File root;
	
	private long instanceCounter;

	@PostConstruct
	public void postConstruct() throws FunctionPreparationException {
		root = new File(rootPath);
		
		File forkVmLib = new File(forkVmLibPath);
		if (!forkVmLib.exists())
			throw new FunctionPreparationException("Fork VM lib missing: " + forkVmLib);
		
		File apiLib = new File(apiLibPath);
		if (!apiLib.exists())
			throw new FunctionPreparationException("API lib missing: " + apiLib);
		
		for (String path : jacksonLibs) {
			if( ! new File(path).exists()) 
				throw new FunctionPreparationException("Jackson lib missing: " + path);
		}
	}


	public WorkspaceResourcesDescriptor prepare(Job job, FunctionDefinition functionDefinition)
			throws FunctionPreparationException {

		File workspace = createWorkspaceFolder(functionDefinition.getFunctionUniqueName(), job.getJobId());
		createConfig(workspace, functionDefinition.getConfig());
		File libFolder = checkOrMakeFolder(new File(workspace, "lib"));
		copyLibs(libFolder, functionDefinition.getLibs());
		File compiledBinFolder = checkOrMakeFolder(new File(workspace, "classes"));
		List<File> faasLibList = new LinkedList<>();
		faasLibList.add(new File(apiLibPath));
		faasLibList.add(new File(forkVmLibPath));
		for (String path : jacksonLibs) {
			faasLibList.add(new File(path));
		}
		File[] faasLibs = faasLibList.toArray(new File[0]);
		compile(functionDefinition, job.getJobId(), compiledBinFolder, libFolder, faasLibs);

		return new WorkspaceResourcesDescriptor(functionDefinition, job, workspace, libFolder, compiledBinFolder,
				faasLibs);
	}
	
	private void createConfig(File workspace, Properties properties) throws FunctionPreparationException {
		File configFile = new File(workspace, "config.txt");
		try (FileOutputStream fos = new FileOutputStream(configFile)) {
			properties.store(fos, "Project Properties");
		} catch (IOException ioex) {
			LOGGER.error("Error creating config.txt", ioex);
			throw new FunctionPreparationException("Error creating config.txt", ioex);
		}
	}

	private void copyLibs(File libFolder, LibResource[] libs) throws FunctionPreparationException {
		try {
			for (LibResource resource : libs) {
				resource.copyResourcesTo(libFolder);
			}
		} catch (IOException ioex) {
			LOGGER.error("Error copying lib resource", ioex);
			throw new FunctionPreparationException("Error copying lib resource", ioex);
		}
	}

	private File createWorkspaceFolder(String functionName, String jobId) throws FunctionPreparationException {
		File definitionFolder = new File(root, functionName);
		File instanceFolder = new File(definitionFolder, "instance-"+instanceCounter++);

		try {
			checkOrMakeFolder(definitionFolder);
			checkOrMakeFolder(instanceFolder);
			return instanceFolder;
		} catch (FunctionPreparationException fpex) {
			throw new FunctionPreparationException("Could not create workspace folder", fpex);
		}

	}

	public void cleanupWorkspaceFolder(File f) throws IOException {
		if (f.isFile()) {
			if (!f.delete()) throw new IOException("Could not delete " + f);
		} 
		else if (f.isDirectory()) {
			File[] children = f.listFiles();
			for (File child : children) {
				cleanupWorkspaceFolder(child);
			}
			f.delete();
		} 
		else
			throw new IOException("What the hell is " + f);
	}

	private File checkOrMakeFolder(File folder) throws FunctionPreparationException {

		if (folder.exists() && folder.isDirectory())
			return folder;

		if (!folder.mkdirs()) {
			throw new FunctionPreparationException("Could not make " + folder.getAbsolutePath());
		}

		return folder;
	}

	public void cleanup(WorkspaceResourcesDescriptor workspaceResourcesDescriptor) {
		// delete the folder recursively
	}

	public void compile(FunctionDefinition definition, String jobId, File destination, File libFolder, File[] faasLibs)
			throws FunctionPreparationException {

		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		StandardJavaFileManager fileManager = compiler.getStandardFileManager(null, null, null);
		List<File> classPathFiles = buildClassPathFiles(definition, libFolder, faasLibs);
		try {
			fileManager.setLocation(StandardLocation.CLASS_OUTPUT, Arrays.asList(destination));
			fileManager.setLocation(StandardLocation.CLASS_PATH, classPathFiles);
			File sourceFile = writeSource(destination, definition);
			
			CharArrayWriter stdOut = new CharArrayWriter();
			
			// Compile the file
			boolean success = compiler.getTask(stdOut, fileManager, null, null, null,
					fileManager.getJavaFileObjectsFromFiles(Arrays.asList(sourceFile))).call();
			fileManager.close();
			
			if( ! success) {
				LOGGER.error("Compile failed. Classpath: <{}>", classPathFiles);
				LOGGER.error("Compiler output: {}", new String(stdOut.toCharArray()));
				throw new FunctionPreparationException("Code did not compile");
			} else {
				LOGGER.debug("Code compiled");
			}
		} catch (IOException ioex) {
			throw new FunctionPreparationException("Could not compile code", ioex);
		}

	}

	private List<File> buildClassPathFiles(FunctionDefinition definition, File libFolder, File[] faasLibs)
			throws FunctionPreparationException {

		List<File> files = new LinkedList<>();
		
		for (int i = 0; i < definition.getLibs().length; i++) {
			LibResource resource = definition.getLibs()[i];
			File file = new File(libFolder, resource.getLibName());
			if (!file.exists())
				throw new FunctionPreparationException("Classpath file missing: " + file);
			files.add(file);
		}
		
		for (int i = 0; i < faasLibs.length; i++) {
			File lib = faasLibs[i];
			if (!lib.exists())
				throw new FunctionPreparationException("FaaS-lib file missing: " + lib);
			files.add(lib);
		}
		return files;
	}

	private File writeSource(File workspaceFolder, FunctionDefinition definition) throws IOException, FunctionPreparationException {
		String[] folderNamesForPackage = definition.getPackageName();
		File currentFolder = workspaceFolder;
		for (String folderName : folderNamesForPackage) {
			currentFolder = new File(currentFolder, folderName);
		}
		checkOrMakeFolder(currentFolder);
		File sourceFile = new File(currentFolder, definition.getFunctionClassName() + ".java");
		try (FileOutputStream fos = new FileOutputStream(sourceFile)) {
			fos.write(definition.getSourceCode().getBytes());
		}
		LOGGER.debug("Wrote source to {}", sourceFile.getAbsolutePath());
		return sourceFile;
	}
}

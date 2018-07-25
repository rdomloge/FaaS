package com.example.faas.reactor.fnloader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.faas.common.Job;

public class ProcessStreamCapture {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStreamCapture.class);
	
	private Process process;
	
	private ExecutorService exec = Executors.newFixedThreadPool(2);
	
	private Reader errorReader;
	
	private Reader inputReader;

	private File workspace;
	
	private Job job;
	
	
	class Reader implements Runnable {
		private InputStream stream;
		private OutputStream output;
		

		public Reader(InputStream stream, OutputStream output) {
			this.stream = stream;
			this.output = output;
		}

		@Override
		public void run() {
			try(InputStreamReader isr = new InputStreamReader(stream); 
					OutputStreamWriter osw = new OutputStreamWriter(output)) {
				
				char[] buf = new char[64];
				int read = 0;
				while((read = isr.read(buf)) != -1) {
					osw.write(buf, 0, read);
				}
				LOGGER.debug("Read complete");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public ProcessStreamCapture(Process process, File workspace, Job job) {
		this.process = process;
		this.workspace = workspace;
		this.job = job;
	}

	public void startCapture() throws FileNotFoundException {
		InputStream errorStream = process.getErrorStream();
		InputStream inputStream = process.getInputStream();
		
		File stdErrFile = new File(workspace, "stdErr-"+job.getJobId()+".log");
		errorReader = new Reader(errorStream, new FileOutputStream(stdErrFile));
		exec.execute(errorReader);
		
		File stdOutFile = new File(workspace, "stdOut-"+job.getJobId()+".log");
		inputReader = new Reader(inputStream, new FileOutputStream(stdOutFile));
		exec.execute(inputReader);
		
		LOGGER.debug("Readers started");
	}
	
	public void cleanUp() {
		exec.shutdownNow();
		LOGGER.debug("Executor stopped");
	}
}

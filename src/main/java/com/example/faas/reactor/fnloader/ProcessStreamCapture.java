package com.example.faas.reactor.fnloader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProcessStreamCapture {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ProcessStreamCapture.class);
	
	private Process process;
	
	private ExecutorService exec = Executors.newFixedThreadPool(2);
	
	private Reader errorReader;
	
	private Reader inputReader;
	
	class Reader implements Runnable {
		private InputStream stream;
		
		private StringBuilder sb = new StringBuilder();

		public Reader(InputStream stream) {
			this.stream = stream;
		}

		@Override
		public void run() {
			try(InputStreamReader isr = new InputStreamReader(stream)) {
				char[] buf = new char[64];
				int read = 0;
				while((read = isr.read(buf)) != -1) {
					sb.append(buf, 0, read);
				}
				LOGGER.debug("Read complete");
			}
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public String getCapture() {
			return sb.toString();
		}
	}

	public ProcessStreamCapture(Process process) {
		this.process = process;
	}

	public void startCapture() {
		InputStream errorStream = process.getErrorStream();
		InputStream inputStream = process.getInputStream();
		
		errorReader = new Reader(errorStream);
		exec.execute(errorReader);
		
		inputReader = new Reader(inputStream);
		exec.execute(inputReader);
		
		LOGGER.debug("Readers started");
	}
	
	public void cleanUp() {
		exec.shutdownNow();
		LOGGER.debug("Executor stopped");
		LOGGER.debug("Stderr\n{}", errorReader.getCapture());
		LOGGER.debug("Stdout\n{}", inputReader.getCapture());
	}
}

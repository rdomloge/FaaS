package com.example.faas.common;

import java.util.concurrent.Callable;

public interface Function<V> extends Callable<V>{
	
	V call();

	void setRequest(JobRequest request);
}

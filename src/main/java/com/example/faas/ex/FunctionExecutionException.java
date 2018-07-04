package com.example.faas.ex;

public class FunctionExecutionException extends FunctionException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1547212871830957723L;

	public FunctionExecutionException() {
	}

	public FunctionExecutionException(String message, Throwable cause) {
		super(message, cause);
	}

	public FunctionExecutionException(String message) {
		super(message);
	}

	public FunctionExecutionException(Throwable cause) {
		super(cause);
	}

}

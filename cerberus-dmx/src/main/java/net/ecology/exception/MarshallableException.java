package net.ecology.exception;

import java.io.Serializable;

public class MarshallableException extends RuntimeException implements Serializable {
	private static final long serialVersionUID = -3774413556076310780L;

	public MarshallableException() {
	}

	public MarshallableException(Throwable cause) {
		super(cause);
	}

	/**
	 *
	 * @param msg exception message
	 */
	public MarshallableException(String msg) {
		super(msg);
	}

}

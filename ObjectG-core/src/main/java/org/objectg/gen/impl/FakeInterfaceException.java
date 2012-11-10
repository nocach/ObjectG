package org.objectg.gen.impl;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class FakeInterfaceException extends RuntimeException {
	public FakeInterfaceException() {
	}

	public FakeInterfaceException(final String message) {
		super(message);
	}

	public FakeInterfaceException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public FakeInterfaceException(final Throwable cause) {
		super(cause);
	}
}

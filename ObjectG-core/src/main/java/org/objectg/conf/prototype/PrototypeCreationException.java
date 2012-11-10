package org.objectg.conf.prototype;

/**
 * User: __nocach
 * Date: 5.11.12
 */
public class PrototypeCreationException extends RuntimeException{
	public PrototypeCreationException() {
	}

	public PrototypeCreationException(final String message) {
		super(message);
	}

	public PrototypeCreationException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PrototypeCreationException(final Throwable cause) {
		super(cause);
	}
}

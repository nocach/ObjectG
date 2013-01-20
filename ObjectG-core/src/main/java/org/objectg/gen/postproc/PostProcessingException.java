package org.objectg.gen.postproc;

/**
 * User: __nocach
 * Date: 20.1.13
 */
public class PostProcessingException extends RuntimeException {
	public PostProcessingException() {
	}

	public PostProcessingException(final String message) {
		super(message);
	}

	public PostProcessingException(final String message, final Throwable cause) {
		super(message, cause);
	}

	public PostProcessingException(final Throwable cause) {
		super(cause);
	}
}

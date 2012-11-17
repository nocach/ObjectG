package org.objectg.gen;

import org.objectg.conf.GenerationConfiguration;

/**
 * User: __nocach
 * Date: 23.9.12
 */
public class GenerationException extends RuntimeException {
    public GenerationException() {
    }

    public GenerationException(String message) {
        super(message);
    }

    public GenerationException(String message, Throwable cause) {
        super(message, cause);
    }
    public GenerationException(String message, GenerationContext context) {
		super(message
				+ " context=" + context
				+ ", hierarchy=" + context.dumpHierarchy());
    }

    public GenerationException(Throwable cause) {
        super(cause);
    }

	public GenerationException(final String message, final GenerationContext<?> context,
			final Throwable e) {
		super(message + " for context="+ context
				+", hierarchy=" + context.dumpHierarchy(), e);
	}

	public GenerationException(final GenerationConfiguration configuration, final GenerationContext context,
			final ClassNotFoundException e) {
		super("configuraiton="+configuration
				+", context="+context
				+", hierarchy=" + context.dumpHierarchy());
	}
}

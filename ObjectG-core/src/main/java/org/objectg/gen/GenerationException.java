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

    public GenerationException(Throwable cause) {
        super(cause);
    }

	public GenerationException(final Throwable e, final GenerationConfiguration configuration,
			final GenerationContext context) {
		super("configuraiton="+configuration+", context="+context);
	}

	public GenerationException(final String message, final GenerationContext<?> context,
			final Throwable e) {
		super(message + " for context="+ context, e);
	}
}

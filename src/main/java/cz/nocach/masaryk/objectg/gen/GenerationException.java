package cz.nocach.masaryk.objectg.gen;

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
}

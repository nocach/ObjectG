package cz.nocach.masaryk.objectg.conf.exception;

/**
 * User: __nocach
 * Date: 5.10.12
 */
public class PrototypeMisuseException extends ConfigurationException {
    public PrototypeMisuseException() {
    }

    public PrototypeMisuseException(String message) {
        super(message);
    }

    public PrototypeMisuseException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrototypeMisuseException(Throwable cause) {
        super(cause);
    }
}

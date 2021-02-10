package io.riskscanner.commons.exception;

public class RSException extends RuntimeException {

    public RSException(String message) {
        super(message);
    }

    private RSException(Throwable t) {
        super(t);
    }

    public static void throwException(String message) {
        throw new RSException(message);
    }

    public static RSException getException(String message) {
        throw new RSException(message);
    }

    public static void throwException(Throwable t) {
        throw new RSException(t);
    }
}

package pl.dashclever.readers.domain;

public class ReaderException extends Exception {

    public ReaderException(Throwable cause, String message) {
        super(message, cause);
    }
    public ReaderException(String message) {
        super(message);
    }

}

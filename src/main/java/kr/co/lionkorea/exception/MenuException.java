package kr.co.lionkorea.exception;

public class MenuException extends RuntimeException{
    public MenuException() {
        super();
    }

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }
}

package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class MenuException extends LionException{
    public MenuException() {
        super();
    }

    public MenuException(String message) {
        super(message);
    }

    public MenuException(String message, Throwable cause) {
        super(message, cause);
    }

    public MenuException(Throwable cause) {
        super(cause);
    }

    public MenuException(HttpStatus status, String message) {
        super(status, message);
    }
}

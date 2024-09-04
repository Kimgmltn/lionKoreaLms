package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class RolesException extends LionException{

    public RolesException() {
        super();
    }

    public RolesException(String message) {
        super(message);
    }

    public RolesException(String message, Throwable cause) {
        super(message, cause);
    }

    public RolesException(Throwable cause) {
        super(cause);
    }

    public RolesException(HttpStatus status, String message) {
        super(status, message);
    }
}

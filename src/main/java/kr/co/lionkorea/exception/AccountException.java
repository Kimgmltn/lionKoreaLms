package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class AccountException extends LionException{
    public AccountException() {
        super();
    }

    public AccountException(String message) {
        super(message);
    }

    public AccountException(String message, Throwable cause) {
        super(message, cause);
    }

    public AccountException(Throwable cause) {
        super(cause);
    }

    public AccountException(HttpStatus status, String message) {
        super(status, message);
    }
}

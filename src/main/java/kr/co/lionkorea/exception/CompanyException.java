package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class CompanyException extends LionException{
    public CompanyException() {
        super();
    }

    public CompanyException(String message) {
        super(message);
    }

    public CompanyException(String message, Throwable cause) {
        super(message, cause);
    }

    public CompanyException(Throwable cause) {
        super(cause);
    }

    public CompanyException(HttpStatus status, String message) {
        super(status, message);
    }
}

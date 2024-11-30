package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class ProjectException extends LionException{

    public ProjectException() {
        super();
    }

    public ProjectException(String message) {
        super(message);
    }

    public ProjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public ProjectException(Throwable cause) {
        super(cause);
    }

    public ProjectException(HttpStatus status, String message) {
        super(status, message);
    }
}

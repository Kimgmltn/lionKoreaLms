package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class FileException extends LionException{

    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public FileException(HttpStatus status, String message) {
        super(status, message);
    }
}

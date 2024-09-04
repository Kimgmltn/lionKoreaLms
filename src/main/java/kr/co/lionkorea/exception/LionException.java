package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class LionException extends RuntimeException{

    private final HttpStatus status;

    public LionException() {
        super();
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LionException(String message) {
        super(message);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LionException(String message, Throwable cause) {
        super(message, cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LionException(Throwable cause) {
        super(cause);
        this.status = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public LionException(HttpStatus status, String message) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus(){
        return this.status;
    }
}

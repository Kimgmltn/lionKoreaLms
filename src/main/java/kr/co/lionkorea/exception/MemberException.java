package kr.co.lionkorea.exception;

import org.springframework.http.HttpStatus;

public class MemberException extends LionException{

    public MemberException() {
        super();
    }

    public MemberException(String message) {
        super(message);
    }

    public MemberException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberException(Throwable cause) {
        super(cause);
    }

    public MemberException(HttpStatus status, String message) {
        super(status, message);
    }
}

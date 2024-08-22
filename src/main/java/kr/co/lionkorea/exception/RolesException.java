package kr.co.lionkorea.exception;

public class RolesException extends RuntimeException{

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
}

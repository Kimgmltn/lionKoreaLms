package kr.co.lionkorea.exception;

import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import org.springframework.http.HttpStatus;

public class FileException extends LionException{

    private DownloadExcelResponse downloadExcelResponse = null;

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

    public FileException(HttpStatus status, String message, DownloadExcelResponse response) {
        super(status, message);
        downloadExcelResponse = response;
    }

    public DownloadExcelResponse getDownloadExcelResponse() {
        return downloadExcelResponse;
    }
}

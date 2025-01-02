package kr.co.lionkorea.exception;

import kr.co.lionkorea.dto.CustomErrorResponse;
import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {LionException.class})
    public ResponseEntity<CustomErrorResponse> handlerException(LionException ex){
        CustomErrorResponse response = CustomErrorResponse.builder()
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(ex.getStatus()).body(response);
    }

    @ExceptionHandler(value = {FileException.class})
    public ResponseEntity<?> handlerFileException(FileException ex){
        DownloadExcelResponse downloadExcelResponse = ex.getDownloadExcelResponse();
        if (downloadExcelResponse == null) {
            handlerException(ex);
        }
        return ResponseEntity.status(ex.getStatus())
                .header("Content-Disposition", "attachment; filename=" + downloadExcelResponse.getObjectName())
                .body(downloadExcelResponse.getBytes());
    }
}

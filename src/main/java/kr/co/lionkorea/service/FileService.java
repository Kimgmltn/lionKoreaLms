package kr.co.lionkorea.service;

import org.springframework.http.ResponseEntity;

import java.util.concurrent.CompletableFuture;

public interface FileService {
    CompletableFuture<ResponseEntity<byte[]>> downloadExcelForm(String fileName);

}

package kr.co.lionkorea.controller;

import kr.co.lionkorea.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileRestController {

    private final FileService fileService;

    @GetMapping("/download/{fileName}")
    public CompletableFuture<ResponseEntity<byte[]>> downloadExcelForm(@PathVariable("fileName") String fileName) {
        return fileService.downloadExcelForm(fileName);
    }
}

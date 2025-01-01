package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/files")
public class FileRestController {

    private final FileService fileService;

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadExcelForm(@PathVariable("fileName") String fileName) {
        DownloadExcelResponse downloadExcelResponse = fileService.downloadExcelForm(fileName);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + downloadExcelResponse.getObjectName() + "\"")
                .body(downloadExcelResponse.getBytes());
    }
}
package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.dto.response.UploadExcelResponse;
import kr.co.lionkorea.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @PostMapping("/upload/members")
    public ResponseEntity<UploadExcelResponse> uploadMemberByExcel(@RequestBody MultipartFile file) {
        return ResponseEntity.ok(fileService.uploadMemberByExcel(file));
    }
}
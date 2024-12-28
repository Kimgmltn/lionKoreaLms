package kr.co.lionkorea.service.impl;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import kr.co.lionkorea.exception.FileException;
import kr.co.lionkorea.repository.FileUrlRepository;
import kr.co.lionkorea.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileUrlRepository fileUrlRepository;
    private final HttpClient httpClient;
    private final ObjectStorageClient

    @Override
    public CompletableFuture<ResponseEntity<byte[]>> downloadExcelForm(String fileName) {
        String keyword = "";
        if (fileName.equals("member")) {
            keyword = "MEMBER";
        }else{
            keyword = "DOMESTIC_COMPANY";
        }

        String downloadUrl = fileUrlRepository.findByKeyword(keyword).orElseThrow(() -> new FileException(HttpStatus.NOT_FOUND, "파일이 없습니다.")).getFileUrl();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(downloadUrl))
                .GET()
                .build();

        return httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofByteArray())
                .thenApply(response -> {
                    if (response.statusCode() == HttpStatus.OK.value()) {
                        return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"downloadedFile\"")
                                .body(response.body());

                    } else {
                        log.error("not HttpStatus 200");
                        throw new FileException(HttpStatus.BAD_REQUEST, "파일 다운로드 실패");
                    }
                })
                .exceptionally(ex -> {
                    log.error("Http Error : {}", ex.getMessage());
                    throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 에러로 인해 다운로드에 실패했습니다.");
                });
    }
}

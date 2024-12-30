package kr.co.lionkorea.service.impl;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import kr.co.lionkorea.domain.FileStorage;
import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.exception.FileException;
import kr.co.lionkorea.repository.FileStorageRepository;
import kr.co.lionkorea.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileStorageRepository fileStorageRepository;
    private final ObjectStorageClient objectStorageClient;

    @Override
    public DownloadExcelResponse downloadExcelForm(String fileName) {

        FileStorage fileStorage = fileStorageRepository.findByObjectName(fileName).orElseThrow(() -> new FileException(HttpStatus.NOT_FOUND, "등록되지 않은 파일입니다."));

        GetNamespaceResponse namespaceResponse = objectStorageClient.getNamespace(GetNamespaceRequest.builder().build());

        String extension = fileStorage.getFileExtension();

        String objectName = fileName + extension;

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .namespaceName(namespaceResponse.getValue())
                .bucketName(fileStorage.getBucketName())
                .objectName("excel/" + objectName)
                .build();

        GetObjectResponse getObjectResponse = objectStorageClient.getObject(getObjectRequest);

        try (InputStream inputStream = getObjectResponse.getInputStream()) {
            return new DownloadExcelResponse(objectName, inputStream.readAllBytes());
        } catch (Exception e) {
            log.error("파일 다운로드 중 오류 message : {}", e.getMessage());
            log.error("파일 다운로드 중 오류 trace: {}", Arrays.toString(e.getStackTrace()));
            throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "파일 다운로드 중 오류가 발생했습니다.");
        }
    }
}

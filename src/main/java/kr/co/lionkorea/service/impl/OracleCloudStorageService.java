package kr.co.lionkorea.service.impl;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import kr.co.lionkorea.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OracleCloudStorageService implements StorageService {

    private final ObjectStorageClient client;
    private final String namespaceName = "";
    private final String bucketName = "";


}

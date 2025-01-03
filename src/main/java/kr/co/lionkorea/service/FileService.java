package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.dto.response.UploadExcelResponse;
import kr.co.lionkorea.dto.response.UploadMemberFileResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {
    DownloadExcelResponse downloadExcelForm(String fileName);
    UploadExcelResponse uploadMemberByExcel(MultipartFile file);

    UploadExcelResponse uploadDomesticCompanyByExcel(MultipartFile file);

    UploadExcelResponse uploadBuyerByExcel(MultipartFile file);
}

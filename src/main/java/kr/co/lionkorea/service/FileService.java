package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.response.DownloadExcelResponse;

public interface FileService {
    DownloadExcelResponse downloadExcelForm(String fileName);
}

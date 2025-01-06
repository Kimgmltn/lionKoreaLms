package kr.co.lionkorea.service.impl;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import kr.co.lionkorea.domain.Buyer;
import kr.co.lionkorea.domain.DomesticCompany;
import kr.co.lionkorea.domain.FileStorage;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.dto.response.UploadExcelResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.exception.FileException;
import kr.co.lionkorea.repository.FileStorageRepository;
import kr.co.lionkorea.service.CompanyService;
import kr.co.lionkorea.service.FileService;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static kr.co.lionkorea.enums.Gender.MALE;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class FileServiceImpl implements FileService {

    private final FileStorageRepository fileStorageRepository;
    private final ObjectStorageClient objectStorageClient;
    private final MemberService memberService;
    private final CompanyService companyService;

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

    @Override
    @Transactional
    public UploadExcelResponse uploadMemberByExcel(MultipartFile file) {
        final String MALE ="남";
        final String ADMIN = "관리자";
        if (file.isEmpty()) {
            throw new FileException(HttpStatus.BAD_REQUEST, "비어있는 파일입니다.");
        }

        // 입력된 데이터 갯수
        int size = 0;
        boolean passable = true;
        try(InputStream inputStream = file.getInputStream()){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);


            Set<String> existEmailList = memberService.findAllMemberEmail();
            Set<String> existLoginIdList = memberService.findAllAccountLoginId();
            List<SaveMemberRequest> saveMemberRequests = new ArrayList<>();
            List<GrantNewAccountRequest> grantNewAccountRequests = new ArrayList<>();

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row, 7)) break;

                String memberName = getCellValue(row.getCell(0));
                String gender = getCellValue(row.getCell(1));
                String email = getCellValue(row.getCell(2));
                String phoneNumber = getCellValue(row.getCell(3));
                String position = getCellValue(row.getCell(4));
                String loginId = getCellValue(row.getCell(5));
                String memo = getCellValue(row.getCell(6));

                StringBuilder errorMessage = new StringBuilder();
                // validation
                if (memberName == null) {
                    errorMessage.append("이름 누락; ");
                }
                if (gender == null) {
                    errorMessage.append("성별 누락; ");
                }
                if (email == null) {
                    errorMessage.append("email 누락; ");
                }
                if (loginId == null) {
                    errorMessage.append("LoginId 누락; ");
                }
                if (email != null && existEmailList.contains(email)) {
                    errorMessage.append("중복된 email; ");
                }
                if (loginId != null && existLoginIdList.contains(loginId)) {
                    errorMessage.append("중복된 loginId; ");
                }

                // 에러가 없으면
                if (passable && errorMessage.isEmpty()) {
                    Gender genderEnum = MALE.equals(gender) ? Gender.MALE : Gender.FEMALE;
                    saveMemberRequests.add(new SaveMemberRequest(memberName, genderEnum, email, phoneNumber, memo));

                    String role = ADMIN.equals(position) ? "admin" : "translator";
                    grantNewAccountRequests.add(new GrantNewAccountRequest(loginId, role));
                }else{
                    passable = false;
                    writeErrorToCell(row, 7, errorMessage.toString().trim());
                }
            }

            if (passable) {
                size = saveMemberRequests.size();
                // 에러가 없을 경우 데이터 삽입
                for (SaveMemberRequest request : saveMemberRequests) {
                    GrantNewAccountRequest accountRequest = grantNewAccountRequests.remove(0);
                    memberService.saveAndGrantNewAccountByExcel(request, accountRequest);
                }
            } else {
                // 에러가 있는 경우 엑셀 반환
                exportErrorExcel(workbook);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");
        }

        String result = size + "개 저장되었습니다.";
        return new UploadExcelResponse(result);
    }

    @Override
    @Transactional
    public UploadExcelResponse uploadDomesticCompanyByExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(HttpStatus.BAD_REQUEST, "비어있는 파일입니다.");
        }

        // 입력된 데이터 갯수
        int size = 0;
        boolean passable = true;
        try(InputStream inputStream = file.getInputStream()){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);


            Set<String> existRegistrationNumbers = companyService.findDomesticCompanyRegistrationNumbers();
            List<SaveCompanyRequest> saveCompanyRequests = new ArrayList<>();

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row, 10)) break;

                String companyName = getCellValue(row.getCell(0));
                String englishName = getCellValue(row.getCell(1));
                String companyRegistrationNumber = getCellValue(row.getCell(2));
                String products = getCellValue(row.getCell(3));
                String manager = getCellValue(row.getCell(4));
                String email = getCellValue(row.getCell(5));
                String phoneNumber = getCellValue(row.getCell(6));
                String homepageUrl = getCellValue(row.getCell(7));
                String roadNameAddress = getCellValue(row.getCell(8));
                String memo = getCellValue(row.getCell(9));

                StringBuilder errorMessage = new StringBuilder();
                // validation
                if (companyName == null) {
                    errorMessage.append("회사명 누락; ");
                }
                if (englishName == null) {
                    errorMessage.append("영문명 누락; ");
                }
                if (companyRegistrationNumber == null) {
                    errorMessage.append("사업자 등록 번호 누락; ");
                }
                if (manager == null) {
                    errorMessage.append("담당자 누락; ");
                }
                if (companyRegistrationNumber != null && existRegistrationNumbers.contains(companyRegistrationNumber)) {
                    errorMessage.append("중복된 사업자 등록 번호; ");
                }

                // 에러가 없으면
                if (passable && errorMessage.isEmpty()) {
                    saveCompanyRequests.add(new SaveCompanyRequest(companyName, englishName, companyRegistrationNumber, roadNameAddress, products, homepageUrl, manager, email, phoneNumber, memo));
                }else{
                    passable = false;
                    writeErrorToCell(row, 10, errorMessage.toString().trim());
                }
            }

            if (passable) {
                size = saveCompanyRequests.size();
                // 에러가 없을 경우 데이터 삽입
                for (SaveCompanyRequest request : saveCompanyRequests) {
                    companyService.saveCompany(DomesticCompany.dtoToEntity(request));
                }
            } else {
                // 에러가 있는 경우 엑셀 반환
                exportErrorExcel(workbook);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");
        }

        String result = size + "개 저장되었습니다.";
        return new UploadExcelResponse(result);
    }

    @Override
    @Transactional
    public UploadExcelResponse uploadBuyerByExcel(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileException(HttpStatus.BAD_REQUEST, "비어있는 파일입니다.");
        }

        // 입력된 데이터 갯수
        int size = 0;
        boolean passable = true;
        try(InputStream inputStream = file.getInputStream()){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);


            Set<String> existRegistrationNumbers = companyService.findBuyerRegistrationNumbers();
            List<SaveCompanyRequest> saveCompanyRequests = new ArrayList<>();

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (isRowEmpty(row, 10)) break;

                String companyName = getCellValue(row.getCell(0));
                String englishName = getCellValue(row.getCell(1));
                String companyRegistrationNumber = getCellValue(row.getCell(2));
                String products = getCellValue(row.getCell(3));
                String manager = getCellValue(row.getCell(4));
                String email = getCellValue(row.getCell(5));
                String phoneNumber = getCellValue(row.getCell(6));
                String homepageUrl = getCellValue(row.getCell(7));
                String roadNameAddress = getCellValue(row.getCell(8));
                String memo = getCellValue(row.getCell(9));

                StringBuilder errorMessage = new StringBuilder();
                // validation
                if (companyName == null) {
                    errorMessage.append("회사명 누락; ");
                }
                if (englishName == null) {
                    errorMessage.append("영문명 누락; ");
                }
                if (companyRegistrationNumber == null) {
                    errorMessage.append("사업자 등록 번호 누락; ");
                }
                if (manager == null) {
                    errorMessage.append("담당자 누락; ");
                }
                if (companyRegistrationNumber != null && existRegistrationNumbers.contains(companyRegistrationNumber)) {
                    errorMessage.append("중복된 사업자 등록 번호; ");
                }

                // 에러가 없으면
                if (passable && errorMessage.isEmpty()) {
                    saveCompanyRequests.add(new SaveCompanyRequest(companyName, englishName, companyRegistrationNumber, roadNameAddress, products, homepageUrl, manager, email, phoneNumber, memo));
                }else{
                    passable = false;
                    writeErrorToCell(row, 10, errorMessage.toString().trim());
                }
            }

            if (passable) {
                size = saveCompanyRequests.size();
                // 에러가 없을 경우 데이터 삽입
                for (SaveCompanyRequest request : saveCompanyRequests) {
                    companyService.saveCompany(Buyer.dtoToEntity(request));
                }
            } else {
                // 에러가 있는 경우 엑셀 반환
                exportErrorExcel(workbook);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다.");
        }

        String result = size + "개 저장되었습니다.";
        return new UploadExcelResponse(result);
    }

    private String getCellValue(Cell cell) {
        if (cell == null || cell.getCellType() == CellType.BLANK) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula();
            default:
                return null; // 처리되지 않은 타입은 null 반환
        }
    }

    private void writeErrorToCell(Row row, int cellIndex, String errorMessage) {
        Cell cell = row.createCell(cellIndex, CellType.STRING);
        cell.setCellValue(errorMessage);
    }

    private void exportErrorExcel(Workbook workbook) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            byte[] excelBytes = outputStream.toByteArray();
            // 클라이언트로 반환하도록 커스텀 예외 생성
            throw new FileException(HttpStatus.BAD_REQUEST, "엑셀 업로드 중 에러가 발생했습니다.", new DownloadExcelResponse("error.xlsx", excelBytes));

        } catch (IOException e) {
            e.printStackTrace();
            throw new FileException(HttpStatus.INTERNAL_SERVER_ERROR, "에러 엑셀 파일 생성 중 오류 발생");
        }
    }

    private boolean isRowEmpty(Row row, int lastCellNum) {
        if (row == null) {
            return true;
        }
        for (int cellIndex = 0; cellIndex < lastCellNum; cellIndex++) {
            Cell cell = row.getCell(cellIndex);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false; // 셀이 비어있지 않음
            }
        }
        return true; // 모든 셀이 비어있음
    }
}

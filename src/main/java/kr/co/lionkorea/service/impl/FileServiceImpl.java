package kr.co.lionkorea.service.impl;

import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import kr.co.lionkorea.domain.FileStorage;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.DownloadExcelResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.dto.response.UploadExcelResponse;
import kr.co.lionkorea.dto.response.UploadMemberFileResponse;
import kr.co.lionkorea.enums.Gender;
import kr.co.lionkorea.exception.AccountException;
import kr.co.lionkorea.exception.FileException;
import kr.co.lionkorea.exception.MemberException;
import kr.co.lionkorea.repository.FileStorageRepository;
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

@Service
@RequiredArgsConstructor
@Slf4j
public class FileServiceImpl implements FileService {

    private final FileStorageRepository fileStorageRepository;
    private final ObjectStorageClient objectStorageClient;
    private final MemberService memberService;

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
        try(InputStream inputStream = file.getInputStream()){
            Workbook workbook = WorkbookFactory.create(inputStream);
            Sheet sheet = workbook.getSheetAt(0);

            List<SaveMemberRequest> saveMemberRequests = new ArrayList<>();
            List<GrantNewAccountRequest> grantNewAccountRequests = new ArrayList<>();
            List<Row> errorRows = new ArrayList<>();

            for (int rowIndex = 2; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null) continue;

                String memberName = getCellValue(row.getCell(0));
                String gender = getCellValue(row.getCell(1));
                String email = getCellValue(row.getCell(2));
                String phoneNumber = getCellValue(row.getCell(3));
                String position = getCellValue(row.getCell(4));
                String loginId = getCellValue(row.getCell(5));
                String memo = getCellValue(row.getCell(6));

                // 필수값 누락 확인
                if (memberName == null) {
                    writeErrorToCell(row, 7, "필수값 누락");
                    errorRows.add(row);
                    continue;
                }
                if (gender == null) {
                    writeErrorToCell(row, 7, "필수값 누락");
                    errorRows.add(row);
                    continue;
                }
                if (email == null) {
                    writeErrorToCell(row, 7, "필수값 누락");
                    errorRows.add(row);
                    continue;
                }
                if (loginId == null) {
                    writeErrorToCell(row, 7, "필수값 누락");
                    errorRows.add(row);
                    continue;
                }

                Gender genderEnum = MALE.equals(gender) ? Gender.MALE : Gender.FEMALE;
                saveMemberRequests.add(new SaveMemberRequest(memberName, genderEnum, email, phoneNumber, memo));

                String role = ADMIN.equals(position) ? "admin" : "translator";
                grantNewAccountRequests.add(new GrantNewAccountRequest(loginId, role));
            }

            size = saveMemberRequests.size();
            // 기존 정보 가져오기
            Set<String> existEmailList = memberService.findAllMemberEmail();
            Set<String> existLoginIdList = memberService.findAllAccountLoginId();

            // 기존 데이터와 중복 되는 데이터 있는지 확인
            for (int i = 0; i < size; i++) {
                SaveMemberRequest saveMemberRequest = saveMemberRequests.get(i);
                GrantNewAccountRequest grantNewAccountRequest = grantNewAccountRequests.get(i);

                // 실제 엑셀 row와 동기화
                Row row = sheet.getRow(i + 2);

                if (existEmailList.contains(saveMemberRequest.getEmail())) {
                    writeErrorToCell(row, 7, "중복된 이메일");
                    errorRows.add(row);
                    continue;
                }

                if (existLoginIdList.contains(grantNewAccountRequest.getLoginId())) {
                    writeErrorToCell(row, 7, "중복된 LoginId");
                    errorRows.add(row);
                    continue;
                }
            }

            if (errorRows.isEmpty()) {
                // 에러가 없을 경우 데이터 삽입
                for (SaveMemberRequest request : saveMemberRequests) {
                    SaveMemberResponse memberResponse = memberService.saveMember(request);
                    Long memberId = memberResponse.getMemberId();

                    // 계정 권한 부여
                    GrantNewAccountRequest accountRequest = grantNewAccountRequests.remove(0);
                    memberService.grantNewAccount(memberId, accountRequest);
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
}

package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Company;
import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.FindCompanyDetailResponse;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import kr.co.lionkorea.exception.CompanyException;
import kr.co.lionkorea.repository.CompanyRepository;
import kr.co.lionkorea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public SaveCompanyResponse saveCompany(SaveCompanyRequest request) {
        Company savedCompany = companyRepository.save(Company.dtoToEntity(request));
        return new SaveCompanyResponse(savedCompany.getId(), "저장되었습니다.");
    }

    @Override
    @Transactional
    public SaveCompanyResponse updateCompany(Long companyId, SaveCompanyRequest request) {
        Company findCompany = companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(HttpStatus.NOT_FOUND, "등록되지 않은 회사입니다."));
        findCompany.changeInfo(request);
        companyRepository.save(findCompany);
        return new SaveCompanyResponse(findCompany.getId(), "수정되었습니다.");
    }

    @Override
    public PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable) {
        return companyRepository.findCompanies(request, pageable);
    }

    @Override
    public FindCompanyDetailResponse findCompanyById(Long companyId) {
        Company findCompany = companyRepository.findById(companyId).orElseThrow(() -> new CompanyException(HttpStatus.NOT_FOUND, "등록되지 않은 회사입니다."));
        return FindCompanyDetailResponse.entityToDto(findCompany);
    }
}

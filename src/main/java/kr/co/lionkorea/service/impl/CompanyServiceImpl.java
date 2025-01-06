package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Buyer;
import kr.co.lionkorea.domain.Company;
import kr.co.lionkorea.domain.DomesticCompany;
import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.FindCompanyDetailResponse;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import kr.co.lionkorea.exception.CompanyException;
import kr.co.lionkorea.repository.CompanyRepository;
import kr.co.lionkorea.service.CompanyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    @Override
    @Transactional
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    @Override
    @Transactional
    public SaveCompanyResponse saveDomesticCompany(SaveCompanyRequest request) {
        Set<String> domesticCompanyRegistrationNumbers = this.findDomesticCompanyRegistrationNumbers();
        if (domesticCompanyRegistrationNumbers.contains(request.getCompanyRegistrationNumber())) {
            log.error("이미 존재하는 사업자 번호 : {}", request.getCompanyRegistrationNumber());
            throw new CompanyException(HttpStatus.CONFLICT, "이미 존재하는 사업자 번호입니다.");
        }
        Company savedCompany = this.saveCompany(DomesticCompany.dtoToEntity(request));
        return new SaveCompanyResponse(savedCompany.getId(), "저장되었습니다.");
    }

    @Override
    @Transactional
    public SaveCompanyResponse saveBuyer(SaveCompanyRequest request) {
        Set<String> buyerRegistrationNumbers = this.findBuyerRegistrationNumbers();
        if (buyerRegistrationNumbers.contains(request.getCompanyRegistrationNumber())) {
            throw new CompanyException(HttpStatus.CONFLICT, "이미 존재하는 바이어ID입니다.");
        }
        Company savedCompany = this.saveCompany(Buyer.dtoToEntity(request));
        return new SaveCompanyResponse(savedCompany.getId(), "저장되었습니다.");
    }

    @Override
    @Transactional
    public SaveCompanyResponse updateCompany(Long companyId, SaveCompanyRequest request, String companyType) {
        Company findCompany = companyRepository.findByIdAndCompanyType(companyId, companyType).orElseThrow(() -> new CompanyException(HttpStatus.NOT_FOUND, "등록되지 않은 회사입니다."));
        findCompany.changeInfo(request);
        companyRepository.save(findCompany);
        return new SaveCompanyResponse(findCompany.getId(), "수정되었습니다.");
    }

    @Override
    @Transactional
    public SaveCompanyResponse updateDomesticCompany(Long companyId, SaveCompanyRequest request) {
        return this.updateCompany(companyId, request, "D");
    }

    @Override
    @Transactional
    public SaveCompanyResponse updateBuyer(Long companyId, SaveCompanyRequest request) {
        return this.updateCompany(companyId, request, "B");
    }

    @Override
    public PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable, String companyType, String companyName) {
        return companyRepository.findCompanies(request, pageable, companyType, companyName);
    }

    @Override
    public PagedModel<FindCompaniesResponse> findDomesticCompanies(FindCompaniesRequest request, Pageable pageable, String companyName) {
        return this.findCompanies(request, pageable, "D", companyName);
    }

    @Override
    public PagedModel<FindCompaniesResponse> findBuyers(FindCompaniesRequest request, Pageable pageable, String companyName) {
        return this.findCompanies(request, pageable, "B", companyName);
    }

    @Override
    public Company findCompanyById(Long companyId, String companyType) {
        return companyRepository.findByIdAndCompanyType(companyId, companyType).orElseThrow(() -> new CompanyException(HttpStatus.NOT_FOUND, "등록되지 않은 회사입니다."));
    }

    @Override
    public FindCompanyDetailResponse findDomesticCompanyById(Long companyId) {
        Company findCompany = this.findCompanyById(companyId, "D");
        return FindCompanyDetailResponse.entityToDto(findCompany);
    }

    @Override
    public FindCompanyDetailResponse findBuyerById(Long companyId) {
        Company findCompany = this.findCompanyById(companyId, "B");
        return FindCompanyDetailResponse.entityToDto(findCompany);
    }

    @Override
    public Set<String> findDomesticCompanyRegistrationNumbers() {
        return companyRepository.findAllByCompanyType("D").stream().map(Company::getCompanyRegistrationNumber).collect(Collectors.toSet());
    }

    @Override
    public Set<String> findBuyerRegistrationNumbers() {
        return companyRepository.findAllByCompanyType("B").stream().map(Company::getCompanyRegistrationNumber).collect(Collectors.toSet());
    }
}

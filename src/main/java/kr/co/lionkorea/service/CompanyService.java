package kr.co.lionkorea.service;

import kr.co.lionkorea.domain.Company;
import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.FindCompanyDetailResponse;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface CompanyService {

//    SaveCompanyResponse saveCompany(SaveCompanyRequest request);

    SaveCompanyResponse saveDomesticCompany(SaveCompanyRequest request);

    SaveCompanyResponse saveBuyer(SaveCompanyRequest request);

    SaveCompanyResponse updateCompany(Long companyId, SaveCompanyRequest request, String companyType);

    PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable, String companyType, String companyName);

    Company findCompanyById(Long companyId, String companyType);

    FindCompanyDetailResponse findDomesticCompanyById(Long companyId);
    FindCompanyDetailResponse findBuyerById(Long companyId);

    PagedModel<FindCompaniesResponse> findDomesticCompanies(FindCompaniesRequest request, Pageable pageable, String companyName);
    PagedModel<FindCompaniesResponse> findBuyers(FindCompaniesRequest request, Pageable pageable, String companyName);

    SaveCompanyResponse updateDomesticCompany(Long companyId, SaveCompanyRequest request);
    SaveCompanyResponse updateBuyer(Long companyId, SaveCompanyRequest request);
}

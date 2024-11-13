package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.dto.response.FindCompanyDetailResponse;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.transaction.annotation.Transactional;

public interface CompanyService {

//    SaveCompanyResponse saveCompany(SaveCompanyRequest request);

    SaveCompanyResponse saveDomesticCompany(SaveCompanyRequest request);

    SaveCompanyResponse saveBuyer(SaveCompanyRequest request);

    SaveCompanyResponse updateCompany(Long companyId, SaveCompanyRequest request);

    PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable, String dType);

    FindCompanyDetailResponse findCompanyById(Long companyId);

    PagedModel<FindCompaniesResponse> findDomesticCompanies(FindCompaniesRequest request, Pageable pageable);
    PagedModel<FindCompaniesResponse> findBuyers(FindCompaniesRequest request, Pageable pageable);
}

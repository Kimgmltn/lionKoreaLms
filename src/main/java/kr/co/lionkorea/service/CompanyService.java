package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;

public interface CompanyService {

    SaveCompanyResponse saveCompany(SaveCompanyRequest request);

    SaveCompanyResponse updateCompany(Long companyId, SaveCompanyRequest request);
}

package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface CompanyQueryDslRepository {

    PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable, String companyType, String companyName);
}

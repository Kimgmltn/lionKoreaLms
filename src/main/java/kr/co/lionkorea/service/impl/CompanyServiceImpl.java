package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Company;
import kr.co.lionkorea.dto.request.SaveCompanyRequest;
import kr.co.lionkorea.dto.response.SaveCompanyResponse;
import kr.co.lionkorea.repository.CompanyRepository;
import kr.co.lionkorea.service.CompanyService;
import lombok.RequiredArgsConstructor;
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
        return new SaveCompanyResponse("저장되었습니다.");
    }
}

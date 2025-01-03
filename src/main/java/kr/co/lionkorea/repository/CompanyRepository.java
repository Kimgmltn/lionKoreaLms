package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyQueryDslRepository {

    Optional<Company> findByIdAndCompanyType(Long companyId, String companyType);

    Set<Company> findAllByCompanyType(String companyType);
}

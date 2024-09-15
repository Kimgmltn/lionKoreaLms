package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long>, CompanyQueryDslRepository {
}

package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long>, AccountQueryDslRepository {
    boolean existsByLoginId(String loginId);

    Optional<Account> findByLoginIdAndUseYnIsTrue(String loginId);

    Account findByLoginIdAndPassword(String loginId, String password);


    Optional<Account> findById(Long accountId);
}

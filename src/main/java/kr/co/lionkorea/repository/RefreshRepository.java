package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.RefreshEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<RefreshEntity, Long> {
    Boolean existsByRefresh(String refreshToken);

    void deleteByRefresh(String refreshToken);
}

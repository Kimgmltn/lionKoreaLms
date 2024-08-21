package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long>, MenuQueryDslRepository {
}

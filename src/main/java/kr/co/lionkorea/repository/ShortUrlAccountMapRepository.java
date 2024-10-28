package kr.co.lionkorea.repository;


import kr.co.lionkorea.domain.ShortUrlAccountMap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShortUrlAccountMapRepository extends JpaRepository<ShortUrlAccountMap, Long> {
    Optional<ShortUrlAccountMap> findByShortUrlAndCreatedDateAfter(String shortUrl, LocalDateTime localDateTime);
}

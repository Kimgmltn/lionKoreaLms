package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.FileUrl;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileUrlRepository extends JpaRepository<FileUrl, Long> {
    Optional<FileUrl> findByKeyword(String keyword);
}

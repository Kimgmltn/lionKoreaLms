package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.FileStorage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileStorageRepository extends JpaRepository<FileStorage, Long> {
    Optional<FileStorage> findByObjectName(String objectName);
}

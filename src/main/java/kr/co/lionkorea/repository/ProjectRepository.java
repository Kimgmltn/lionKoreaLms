package kr.co.lionkorea.repository;

import kr.co.lionkorea.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectQueryDslRepository {
    boolean existsByTranslatorIdAndHourAndTimePeriodAndConsultationDate(Long translatorId, String hour, String timePeriod, String consultationDate);
}

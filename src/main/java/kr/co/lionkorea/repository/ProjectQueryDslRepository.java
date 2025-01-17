package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.request.FindProjectsForAdminRequest;
import kr.co.lionkorea.dto.request.FindProjectsForTranslatorRequest;
import kr.co.lionkorea.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.Optional;

public interface ProjectQueryDslRepository {
    FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId);

    PagedModel<FindProjectsForAdminResponse> findProjectsForAdmin(FindProjectsForAdminRequest request, Pageable pageable);

    PagedModel<FindProjectsForTranslatorResponse> findProjectsForTranslator(FindProjectsForTranslatorRequest request, Pageable pageable, Long accountId);

    Optional<FindProjectDetailForTranslatorResponse> findProjectDetailForTranslator(Long projectId, Long accountId);

    PagedModel<FindProjectsByCompanyIdResponse> findProjectByCompanyId(Long companyId, Pageable pageable);
}

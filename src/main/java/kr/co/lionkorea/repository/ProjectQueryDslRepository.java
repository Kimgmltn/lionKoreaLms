package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.request.FindProjectsForAdminRequest;
import kr.co.lionkorea.dto.request.FindProjectsForTranslatorRequest;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.dto.response.FindProjectsForAdminResponse;
import kr.co.lionkorea.dto.response.FindProjectsForTranslatorResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface ProjectQueryDslRepository {
    FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId);

    PagedModel<FindProjectsForAdminResponse> findProjectsForAdmin(FindProjectsForAdminRequest request, Pageable pageable);

    PagedModel<FindProjectsForTranslatorResponse> findProjectsForTranslator(FindProjectsForTranslatorRequest request, Pageable pageable);
}

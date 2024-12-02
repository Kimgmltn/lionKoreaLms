package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.FindProjectsForAdminRequest;
import kr.co.lionkorea.dto.request.FindProjectsForTranslatorRequest;
import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.dto.request.SaveRejectProjectRequest;
import kr.co.lionkorea.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface ProjectService {
    SaveProjectResponse saveProject(SaveProjectRequest request);

    FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId);

    SaveRejectProjectResponse rejectProject(Long projectId, SaveRejectProjectRequest request);

    PagedModel<FindProjectsForAdminResponse> findProjectsForAdmin(FindProjectsForAdminRequest request, Pageable pageable);

    PagedModel<FindProjectsForTranslatorResponse> findProjectsForTranslator(FindProjectsForTranslatorRequest request, Pageable pageable, CustomUserDetails userDetails);

    FindProjectDetailForTranslatorResponse findProjectDetailForTranslator(Long projectId, CustomUserDetails userDetails);

    Void startConsultation(Long projectId);
}

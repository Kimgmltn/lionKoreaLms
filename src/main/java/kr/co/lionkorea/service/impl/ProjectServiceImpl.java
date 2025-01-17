package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Project;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.*;
import kr.co.lionkorea.exception.ProjectException;
import kr.co.lionkorea.repository.ProjectRepository;
import kr.co.lionkorea.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public SaveProjectResponse saveProject(SaveProjectRequest request) {
        if(projectRepository.existsByTranslatorIdAndHour(request.getTranslatorId(), request.getHour())){
            throw new ProjectException(HttpStatus.NOT_FOUND, "담당자가 동일한 시간에<br/>다른 프로젝트에 임명되어있습니다");
        };
        Project project = projectRepository.save(Project.dtoToEntity(request));
        return new SaveProjectResponse(project.getId(), "저장되었습니다.");
    }

    @Override
    public FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId) {
        return projectRepository.findProjectDetailForAdmin(projectId);
    }

    @Override
    @Transactional
    public SaveRejectProjectResponse rejectProject(Long projectId, SaveRejectProjectRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."));
        project.rejectProject(request.getRejectReason());
        projectRepository.save(project);
        return new SaveRejectProjectResponse("반려되었습니다.");
    }

    @Override
    public PagedModel<FindProjectsForAdminResponse> findProjectsForAdmin(FindProjectsForAdminRequest request, Pageable pageable) {
        return projectRepository.findProjectsForAdmin(request, pageable);
    }

    @Override
    public PagedModel<FindProjectsForTranslatorResponse> findProjectsForTranslator(FindProjectsForTranslatorRequest request, Pageable pageable, CustomUserDetails userDetails) {
        return projectRepository.findProjectsForTranslator(request, pageable, userDetails.getAccountId());
    }

    @Override
    public FindProjectDetailForTranslatorResponse findProjectDetailForTranslator(Long projectId, CustomUserDetails userDetails) {
        return projectRepository.findProjectDetailForTranslator(projectId, userDetails.getAccountId()).orElseThrow(()-> new ProjectException(HttpStatus.NOT_ACCEPTABLE, "해당 프로젝트의 담당자가 아닙니다."));
    }

    @Override
    @Transactional
    public Void startConsultation(Long projectId) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."));
        project.startProject();
        projectRepository.save(project);
        return null;
    }

    @Override
    @Transactional
    public SaveCompleteConsultationResponse completeConsultation(Long projectId, SaveCompleteConsultationRequest request) {
        Project project = projectRepository.findById(projectId).orElseThrow(() -> new ProjectException(HttpStatus.NOT_FOUND, "존재하지 않는 프로젝트입니다."));
        project.completeProject(request.getConsultationNotes());
        projectRepository.save(project);
        return new SaveCompleteConsultationResponse("저장되었습니다.");
    }

    @Override
    public PagedModel<FindProjectsByCompanyIdResponse> findProjectByCompanyId(Long companyId, Pageable pageable) {
        return projectRepository.findProjectByCompanyId(companyId, pageable);
    }
}

package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Project;
import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.dto.request.SaveRejectProjectRequest;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.dto.response.SaveProjectResponse;
import kr.co.lionkorea.dto.response.SaveRejectProjectResponse;
import kr.co.lionkorea.exception.ProjectException;
import kr.co.lionkorea.repository.ProjectRepository;
import kr.co.lionkorea.service.ProjectService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}

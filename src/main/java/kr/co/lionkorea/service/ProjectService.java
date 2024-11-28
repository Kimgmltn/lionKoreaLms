package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.SaveProjectRequest;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.dto.response.SaveProjectResponse;

public interface ProjectService {
    SaveProjectResponse saveProject(SaveProjectRequest request);

    FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId);
}

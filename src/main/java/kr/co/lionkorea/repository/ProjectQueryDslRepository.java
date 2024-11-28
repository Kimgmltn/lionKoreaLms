package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;

public interface ProjectQueryDslRepository {
    FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId);
}

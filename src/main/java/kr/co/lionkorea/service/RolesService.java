package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.MatchRolesAndMenusRequest;
import kr.co.lionkorea.dto.request.SaveRoleRequest;
import kr.co.lionkorea.dto.response.MatchRolesAndMenusResponse;
import kr.co.lionkorea.dto.response.SaveRoleResponse;
import org.springframework.transaction.annotation.Transactional;

public interface RolesService {
    MatchRolesAndMenusResponse matchRolesAndMenus(MatchRolesAndMenusRequest request);

    @Transactional
    SaveRoleResponse saveRole(SaveRoleRequest request);
}

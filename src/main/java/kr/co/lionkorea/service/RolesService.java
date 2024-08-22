package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.MatchRolesAndMenusRequest;
import kr.co.lionkorea.dto.response.MatchRolesAndMenusResponse;

public interface RolesService {
    MatchRolesAndMenusResponse matchRolesAndMenus(MatchRolesAndMenusRequest request);
}

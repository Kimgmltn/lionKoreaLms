package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.enums.Role;

import java.util.List;
import java.util.Set;

public interface MenuQueryDslRepository {
    List<FindMenuResponse> findMenusByRoleNames(Set<Role> roleNames);
}

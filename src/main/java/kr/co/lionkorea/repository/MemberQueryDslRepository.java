package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.response.FindMembersResponse;
import kr.co.lionkorea.enums.Role;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface MemberQueryDslRepository {

    PagedModel<FindMembersResponse> findMembersByRolePaging(Role role, Pageable pageable);
}

package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.request.FindMembersByRoleRequest;
import kr.co.lionkorea.dto.response.FindMembersByRoleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MemberQueryDslRepository {

    Page<FindMembersByRoleResponse> findMembersByRolePaging(FindMembersByRoleRequest request, Pageable pageable);
}

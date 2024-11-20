package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.response.FindMembersResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

public interface MemberQueryDslRepository {

    PagedModel<FindMembersResponse> findMembersPaging(FindMembersRequest request, Pageable pageable, String memberName);
}

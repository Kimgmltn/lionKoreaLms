package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMembersByRoleResponse;
import kr.co.lionkorea.dto.response.GrantNewAccountResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    PagedModel<FindMembersByRoleResponse> findMembersByRole(String roleName, Pageable pageable);
    void saveMemberAll(List<SaveMemberRequest> requests);

    GrantNewAccountResponse grantNewAccount(GrantNewAccountRequest request);

    MemberDetails findUserDetails(String username);

    PagedModel<FindMembersByRoleResponse> findMembers(FindMembersRequest request, Pageable pageable);
}

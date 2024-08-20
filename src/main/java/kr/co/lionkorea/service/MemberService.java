package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMemberResponse;
import kr.co.lionkorea.dto.response.GrantNewAccountResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;

import java.util.List;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    FindMemberResponse findAllMember();
    void saveMemberAll(List<SaveMemberRequest> requests);

    GrantNewAccountResponse grantNewAccount(GrantNewAccountRequest request);

    MemberDetails findUserDetails(String username);
}

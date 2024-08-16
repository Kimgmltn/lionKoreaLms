package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMemberResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;

import java.util.List;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    FindMemberResponse findAllMember();
    void saveMemberAll(List<SaveMemberRequest> requests);
}

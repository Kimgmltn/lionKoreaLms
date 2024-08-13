package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMemberResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    FindMemberResponse findAllMember();
}

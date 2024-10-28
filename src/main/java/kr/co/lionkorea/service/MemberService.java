package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveAccountDetailRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    void saveMemberAll(List<SaveMemberRequest> requests);

    GrantNewAccountResponse grantNewAccount(Long memberId, GrantNewAccountRequest request);

    boolean isExistsLoginId(String loginId);

    MemberDetails findUserDetails(String username);

    PagedModel<FindMembersResponse> findMembers(FindMembersRequest request, Pageable pageable);

    FindMemberDetailResponse findMemberById(Long memberId);

    List<FindMemberByAccountResponse> findMemberAccount(Long memberId);

    DecodeShortUrlResponse decodeShortUrl(String shortUrl);

    SaveAccountDetailResponse updateAccountDetail(Long memberId, Long accountId, SaveAccountDetailRequest request);
}

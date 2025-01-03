package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface MemberService {
    SaveMemberResponse saveMember(SaveMemberRequest request);

    SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request);

    void saveMemberAll(List<SaveMemberRequest> requests);

    GrantNewAccountResponse grantNewAccount(Long memberId, GrantNewAccountRequest request);

    boolean isExistsLoginId(String loginId);

    MemberDetails findUserDetails(String username);

    PagedModel<FindMembersResponse> findMembers(FindMembersRequest request, Pageable pageable, String memberName);

    FindMemberDetailResponse findMemberById(Long memberId);

    List<FindMemberByAccountResponse> findMemberAccount(Long memberId);

    DecodeShortUrlResponse decodeShortUrl(String shortUrl);

    SaveAccountDetailResponse updateAccountDetail(Long memberId, Long accountId, SaveAccountDetailRequest request);

    SavePasswordResponse updatePassword(Long accountId, SavePasswordRequest request);

    void deleteShortUrlAccountIdMap(Long accountId);

    PagedModel<FindTranslatorsResponse> findTranslators(Pageable pageable, String memberName);

    GrantNewAccountResponse rePassword(Long accountId);

    Set<String> findAllMemberEmail();
    Set<String> findAllAccountLoginId();

    @Transactional
    void saveAndGrantNewAccountByExcel(SaveMemberRequest memberRequest, GrantNewAccountRequest grantNewAccountRequest);
}

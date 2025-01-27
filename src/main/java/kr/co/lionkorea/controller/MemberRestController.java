package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.*;
import kr.co.lionkorea.dto.response.*;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class MemberRestController {

    public final MemberService memberService;

    @PostMapping("/save")
    public ResponseEntity<SaveMemberResponse> saveMember(@RequestBody SaveMemberRequest request){
        return ResponseEntity.ok(memberService.saveMember(request));
    }

    @PatchMapping("/save/{memberId}")
    public ResponseEntity<SaveMemberResponse> updateMember(@PathVariable("memberId") Long memberId, @RequestBody SaveMemberRequest request) {
        return ResponseEntity.ok(memberService.updateMember(memberId, request));
    }

    @GetMapping()
    public ResponseEntity<PagedModel<FindMembersResponse>> findMembers(@ModelAttribute FindMembersRequest request, Pageable pageable, @RequestParam(value = "memberName", required = false) String memberName){
        return ResponseEntity.ok(memberService.findMembers(request, pageable, memberName));
    }

    @GetMapping("/translator")
    public ResponseEntity<PagedModel<FindTranslatorsResponse>> findTranslators(Pageable pageable, @RequestParam(value = "memberName", required = false) String memberName){
        return ResponseEntity.ok(memberService.findTranslators(pageable, memberName));
    }

    @GetMapping("/{memberId}/detail")
    public ResponseEntity<FindMemberDetailResponse> findMemberById(@PathVariable(value = "memberId") Long memberId){
        return ResponseEntity.ok(memberService.findMemberById(memberId));
    }

    @PostMapping("/{memberId}/newAccount")
    public ResponseEntity<GrantNewAccountResponse> grantNewAccount(@PathVariable Long memberId, @RequestBody GrantNewAccountRequest request){
        return ResponseEntity.ok(memberService.grantNewAccount(memberId, request));
    }

    @PatchMapping("/{accountId}/rePassword")
    public ResponseEntity<GrantNewAccountResponse> rePassword(@PathVariable Long accountId){
        return ResponseEntity.ok(memberService.rePassword(accountId));
    }

    @GetMapping("/{memberId}/accounts")
    public ResponseEntity<List<FindMemberByAccountResponse>> findMemberAccount(@PathVariable(value = "memberId") Long memberId){
        return ResponseEntity.ok(memberService.findMemberAccount(memberId));
    }

    @PatchMapping("/{memberId}/accounts/{accountId}")
    public ResponseEntity<SaveAccountDetailResponse> updateAccountDetail(@PathVariable Long memberId, @PathVariable Long accountId, @RequestBody SaveAccountDetailRequest request){
        return ResponseEntity.ok(memberService.updateAccountDetail(memberId, accountId, request));
    }

    @PatchMapping("/accounts/{accountId}/password")
    public ResponseEntity<SavePasswordResponse> updatePassword(@PathVariable Long accountId, @RequestBody SavePasswordRequest request){
        return ResponseEntity.ok(memberService.updatePassword(accountId, request));
    }

    @GetMapping("/{shortUrl}/valid")
    public ResponseEntity<DecodeShortUrlResponse> decodeShortUrl(@PathVariable String shortUrl){
        return ResponseEntity.ok(memberService.decodeShortUrl(shortUrl));
    }
}

package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.request.GrantNewAccountRequest;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.*;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<PagedModel<FindMembersResponse>> findMembers(@ModelAttribute FindMembersRequest request, Pageable pageable){
        return ResponseEntity.ok(memberService.findMembers(request, pageable));
    }

    @GetMapping("/{memberId}/detail")
    public ResponseEntity<FindMemberDetailResponse> findMemberById(@PathVariable(value = "memberId") Long memberId){
        return ResponseEntity.ok(memberService.findMemberById(memberId));
    }
//    @GetMapping("/{roleName}")
//    public ResponseEntity<PagedModel<FindMembersByRoleResponse>> findMembersByRole(@PathVariable(value = "roleName") String roleName, Pageable pageable) {
//        return ResponseEntity.ok(memberService.findMembersByRole(roleName, pageable));
//    }

    @PostMapping("/newAccount")
    public ResponseEntity<GrantNewAccountResponse> grantNewAccount(@RequestBody GrantNewAccountRequest request){
        return ResponseEntity.ok(memberService.grantNewAccount(request));

    }

    @GetMapping("/{memberId}/accounts")
    public ResponseEntity<List<FindMemberByAccountResponse>> findMemberAccount(@PathVariable(value = "memberId") Long memberId){
        return ResponseEntity.ok(memberService.findMemberAccount(memberId));
    }

    @PatchMapping("/{memberId}/updatePassword")
    public ResponseEntity<?> updatePassword(@PathVariable(value = "memberId") Long memberId){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{shortUrl}/valid")
    public ResponseEntity<DecodeShortUrlResponse> decodeShortUrl(@PathVariable String shortUrl){
        return ResponseEntity.ok(memberService.decodeShortUrl(shortUrl));
    }

}

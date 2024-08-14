package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMemberResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/member")
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

    @GetMapping
    public ResponseEntity<FindMemberResponse> findAllMember() {
        return ResponseEntity.ok(memberService.findAllMember());
    }

}

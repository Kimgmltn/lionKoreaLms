package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Member;
import kr.co.lionkorea.dto.request.SaveMemberRequest;
import kr.co.lionkorea.dto.response.FindMemberResponse;
import kr.co.lionkorea.dto.response.SaveMemberResponse;
import kr.co.lionkorea.exception.MemberException;
import kr.co.lionkorea.repository.MemberRepository;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    @Transactional
    public void saveMemberAll(List<SaveMemberRequest> requests) {
        List<Member> collect = requests.stream().map(request -> Member.dtoToEntity(request)).collect(Collectors.toList());
        memberRepository.saveAll(collect);
    }

    @Override
    @Transactional
    public SaveMemberResponse saveMember(SaveMemberRequest request) {
        Member savedMember = memberRepository.save(Member.dtoToEntity(request));
        return new SaveMemberResponse(savedMember.getId(), "저장되었습니다.");
    }

    @Override
    @Transactional
    public SaveMemberResponse updateMember(Long memberId, SaveMemberRequest request) {
        Member findMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberException("등록되지 않은 회원입니다."));
        findMember.changeInfo(request);
        memberRepository.save(findMember);
        return new SaveMemberResponse(findMember.getId(), "수정되었습니다.");
    }

    @Override
    public FindMemberResponse findAllMember() {
        List<Member> members = memberRepository.findAll();
        return null;
    }
}

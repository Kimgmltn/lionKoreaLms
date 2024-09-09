package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.response.FindMemberByAccountResponse;

import java.util.List;

public interface AccountQueryDslRepository {
    List<FindMemberByAccountResponse> findByMemberIdWithAccount(Long memberId);
}

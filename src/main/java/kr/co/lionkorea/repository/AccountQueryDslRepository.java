package kr.co.lionkorea.repository;

import kr.co.lionkorea.dto.response.FindTranslatorsResponse;
import kr.co.lionkorea.dto.response.FindMemberByAccountResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

public interface AccountQueryDslRepository {
    List<FindMemberByAccountResponse> findByMemberIdWithAccount(Long memberId);

    PagedModel<FindTranslatorsResponse> findTranslatorsPaging(Pageable pageable, String memberName);
}

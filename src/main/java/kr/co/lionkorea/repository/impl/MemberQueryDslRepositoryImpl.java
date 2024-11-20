package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.request.FindMembersRequest;
import kr.co.lionkorea.dto.response.FindMembersResponse;
import kr.co.lionkorea.repository.MemberQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static kr.co.lionkorea.domain.QMember.member;

@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public PagedModel<FindMembersResponse> findMembersPaging(FindMembersRequest request, Pageable pageable, String memberName) {

        List<FindMembersResponse> result = query.select(Projections.fields(FindMembersResponse.class,
                        member.id.as("memberId"),
                        member.gender.as("gender"),
                        member.memberName.as("memberName"),
                        member.email.as("email")))
                .from(member)
                .where(memberNameContainsIgnoreCase(memberName))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(query.select(member.count())
                .from(member)
                .where(memberNameContainsIgnoreCase(memberName))
                .fetchOne())
                .orElse(0L);

        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }

    private Predicate memberNameContainsIgnoreCase(String memberName) {
        return StringUtils.hasText(memberName) ? member.memberName.containsIgnoreCase(memberName) : null;
    }
}

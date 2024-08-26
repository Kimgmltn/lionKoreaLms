package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.request.FindMembersByRoleRequest;
import kr.co.lionkorea.dto.response.FindMembersByRoleResponse;
import kr.co.lionkorea.repository.MemberQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static kr.co.lionkorea.domain.QAccount.account;
import static kr.co.lionkorea.domain.QAccountRole.accountRole;
import static kr.co.lionkorea.domain.QMember.member;
import static kr.co.lionkorea.domain.QRoles.roles;

@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public Page<FindMembersByRoleResponse> findMembersByRolePaging(FindMembersByRoleRequest request, Pageable pageable) {

        BooleanExpression condition = roles.roleName.eq(request.getRole());

        List<FindMembersByRoleResponse> result = query.select(Projections.fields(FindMembersByRoleResponse.class,
                        member.id.as("memberId"),
                        member.gender.as("gender"),
                        member.memberName.as("memberName"),
                        member.email.as("email")))
                .from(member)
                .innerJoin(account).on(member.id.eq(account.member.id).and(account.useYn.isTrue()))
                .innerJoin(accountRole).on(account.id.eq(accountRole.account.id))
                .innerJoin(roles).on(accountRole.roles.id.eq(roles.id))
                .where(condition)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = query.select(Expressions.constant(1))
                .from(member)
                .innerJoin(account).on(member.id.eq(account.member.id).and(account.useYn.isTrue()))
                .innerJoin(accountRole).on(account.id.eq(accountRole.account.id))
                .innerJoin(roles).on(accountRole.roles.id.eq(roles.id))
                .where(condition)
                .fetch().size();
        return new PageImpl<>(result, pageable, total);
    }


}

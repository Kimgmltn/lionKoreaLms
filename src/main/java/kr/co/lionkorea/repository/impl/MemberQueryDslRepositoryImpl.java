package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.response.FindMembersResponse;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.MemberQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

import static kr.co.lionkorea.domain.QAccount.account;
import static kr.co.lionkorea.domain.QAccountRole.accountRole;
import static kr.co.lionkorea.domain.QMember.member;
import static kr.co.lionkorea.domain.QRoles.roles;

@RequiredArgsConstructor
public class MemberQueryDslRepositoryImpl implements MemberQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public PagedModel<FindMembersResponse> findMembersByRolePaging(Role role, Pageable pageable) {

        BooleanExpression condition = (role != null) ? roles.roleName.eq(role) : null;

        List<FindMembersResponse> result = query.select(Projections.fields(FindMembersResponse.class,
                        member.id.as("memberId"),
                        member.gender.as("gender"),
                        member.memberName.as("memberName"),
                        member.email.as("email")))
                .from(member)
                .innerJoin(account).on(member.id.eq(account.member.id).and(account.useYn.isTrue()))
                .innerJoin(accountRole).on(account.id.eq(accountRole.account.id))
                .innerJoin(roles).on(accountRole.roles.id.eq(roles.id))
                .where(condition)
                .orderBy(member.id.desc())
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


        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }


}

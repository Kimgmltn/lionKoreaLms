package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.response.FindTranslatorsResponse;
import kr.co.lionkorea.dto.response.FindMemberByAccountResponse;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.AccountQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

import static kr.co.lionkorea.domain.QAccount.account;
import static kr.co.lionkorea.domain.QAccountRole.accountRole;
import static kr.co.lionkorea.domain.QRoles.roles;
import static kr.co.lionkorea.domain.QMember.member;

@RequiredArgsConstructor
public class AccountQueryDslRepositoryImpl implements AccountQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public List<FindMemberByAccountResponse> findByMemberIdWithAccount(Long memberId) {
        return query
                .selectDistinct(Projections.fields(FindMemberByAccountResponse.class,
                        account.id.as("accountId")
                        , account.loginId
                        , roles.roleName.as("role")
                        , account.useYn
                        , account.expireDate
                        , account.loginId
                        , account.joinDate
                        , account.randomPasswordChangeYn.as("passwordChangeYn")
                ))
                .from(account)
                .innerJoin(account.accountRoles, accountRole)
                .innerJoin(accountRole.roles, roles)
                .where(account.member.id.eq(memberId))
                .orderBy(account.id.desc())
                .fetch();
    }

    @Override
    public PagedModel<FindTranslatorsResponse> findTranslatorsPaging(Pageable pageable, String memberName) {
        List<FindTranslatorsResponse> result = query.select(Projections.fields(FindTranslatorsResponse.class,
                        account.id.as("accountId"),
                        member.memberName
                        ))
                .from(account)
                .innerJoin(account.member, member)
                .innerJoin(account.accountRoles, accountRole)
                .innerJoin(accountRole.roles, roles)
                .where(roles.roleName.eq(Role.ROLE_TRANSLATOR),
                        memberNameContainsIgnoreCase(memberName))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(query.select(account.count())
                        .from(account)
                        .innerJoin(account.member, member)
                        .innerJoin(account.accountRoles, accountRole)
                        .innerJoin(accountRole.roles, roles)
                        .where(roles.roleName.eq(Role.ROLE_TRANSLATOR),
                                memberNameContainsIgnoreCase(memberName))
                        .fetchOne())
                .orElse(0L);
        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }

    private Predicate memberNameContainsIgnoreCase(String memberName) {
        return StringUtils.hasText(memberName) ? member.memberName.containsIgnoreCase(memberName) : null;
    }
}

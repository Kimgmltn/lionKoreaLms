package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.response.FindMemberByAccountResponse;
import kr.co.lionkorea.repository.AccountQueryDslRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static kr.co.lionkorea.domain.QAccount.account;
import static kr.co.lionkorea.domain.QAccountRole.accountRole;
import static kr.co.lionkorea.domain.QRoles.roles;

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

}

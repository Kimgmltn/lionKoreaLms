package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.domain.QMember;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.repository.ProjectQueryDslRepository;
import lombok.RequiredArgsConstructor;

import static kr.co.lionkorea.domain.QAccount.account;
import static kr.co.lionkorea.domain.QBuyer.buyer;
import static kr.co.lionkorea.domain.QDomesticCompany.domesticCompany;
import static kr.co.lionkorea.domain.QMember.member;
import static kr.co.lionkorea.domain.QProject.project;

@RequiredArgsConstructor
public class ProjectQueryDslRepositoryImpl implements ProjectQueryDslRepository {
    private final JPAQueryFactory query;

    @Override
    public FindProjectDetailForAdminResponse findProjectDetailForAdmin(Long projectId) {
        return query
                .select(Projections.fields(FindProjectDetailForAdminResponse.class,
                        project.id.as("projectId"),
                        buyer.companyName.as("buyerName"),
                        domesticCompany.companyName.as("domesticCompanyName"),
                        project.projectName.as("projectName"),
                        member.memberName.as("translatorName"),
                        project.language,
                        project.processStatus,
                        project.consultationDate,
                        project.timePeriod,
                        project.hour,
                        project.minute,
                        project.consultationNotes
                ))
                .from(project)
                .innerJoin(project.buyer, buyer)
                .innerJoin(project.domesticCompany, domesticCompany)
                .innerJoin(project.translator, account)
                .innerJoin(account.member, member)
                .where(project.id.eq(projectId))
                .fetchFirst();
    }

}

package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.dto.request.FindProjectsForAdminRequest;
import kr.co.lionkorea.dto.request.FindProjectsForTranslatorRequest;
import kr.co.lionkorea.dto.response.FindProjectDetailForAdminResponse;
import kr.co.lionkorea.dto.response.FindProjectDetailForTranslatorResponse;
import kr.co.lionkorea.dto.response.FindProjectsForAdminResponse;
import kr.co.lionkorea.dto.response.FindProjectsForTranslatorResponse;
import kr.co.lionkorea.repository.ProjectQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;
import java.util.Optional;

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
                        project.consultationNotes,
                        project.rejectReason
                ))
                .from(project)
                .innerJoin(project.buyer, buyer)
                .innerJoin(project.domesticCompany, domesticCompany)
                .innerJoin(project.translator, account)
                .innerJoin(account.member, member)
                .where(project.id.eq(projectId))
                .fetchFirst();
    }

    @Override
    public PagedModel<FindProjectsForAdminResponse> findProjectsForAdmin(FindProjectsForAdminRequest request, Pageable pageable) {
        List<FindProjectsForAdminResponse> result = query
                .select(Projections.fields(FindProjectsForAdminResponse.class,
                        project.id.as("projectId"),
                        buyer.companyName.as("buyerName"),
                        domesticCompany.companyName.as("domesticCompanyName"),
                        project.projectName.as("projectName"),
                        member.memberName.as("translatorName"),
                        project.language,
                        project.processStatus,
                        project.timePeriod,
                        project.hour,
                        project.minute,
                        project.rejectReason
                ))
                .from(project)
                .innerJoin(project.buyer, buyer)
                .innerJoin(project.domesticCompany, domesticCompany)
                .innerJoin(project.translator, account)
                .innerJoin(account.member, member)
                .where(project.consultationDate.eq(request.getConsultationDate()))
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(query.select(project.count())
                        .from(project)
                        .where(project.consultationDate.eq(request.getConsultationDate()))
                        .fetchOne())
                .orElse(0L);
        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }

    @Override
    public PagedModel<FindProjectsForTranslatorResponse> findProjectsForTranslator(FindProjectsForTranslatorRequest request, Pageable pageable, Long accountId) {
        List<FindProjectsForTranslatorResponse> result = query
                .select(Projections.fields(FindProjectsForTranslatorResponse.class,
                        project.id.as("projectId"),
                        buyer.companyName.as("buyerName"),
                        domesticCompany.companyName.as("domesticCompanyName"),
                        project.projectName.as("projectName"),
                        member.memberName.as("translatorName"),
                        project.language,
                        project.processStatus,
                        project.timePeriod,
                        project.hour,
                        project.minute,
                        project.rejectReason
                ))
                .from(project)
                .innerJoin(project.buyer, buyer)
                .innerJoin(project.domesticCompany, domesticCompany)
                .innerJoin(project.translator, account)
                .innerJoin(account.member, member)
                .where(
                        account.id.eq(accountId),
                        project.consultationDate.eq(request.getConsultationDate())
                )
                .orderBy(project.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = Optional.ofNullable(query.select(project.count())
                        .from(project)
                        .innerJoin(project.translator, account)
                        .where(
                                account.id.eq(accountId),
                                project.consultationDate.eq(request.getConsultationDate())
                        )
                        .fetchOne())
                .orElse(0L);
        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }

    @Override
    public Optional<FindProjectDetailForTranslatorResponse> findProjectDetailForTranslator(Long projectId, Long accountId) {
        return Optional.ofNullable(query
                .select(Projections.fields(FindProjectDetailForTranslatorResponse.class,
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
                        project.consultationNotes,
                        project.rejectReason
                ))
                .from(project)
                .innerJoin(project.buyer, buyer)
                .innerJoin(project.domesticCompany, domesticCompany)
                .innerJoin(project.translator, account)
                .innerJoin(account.member, member)
                .where(
                        project.id.eq(projectId),
                        account.id.eq(accountId)
                )
                .fetchFirst());
    }
}

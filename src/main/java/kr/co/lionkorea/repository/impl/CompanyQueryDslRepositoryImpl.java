package kr.co.lionkorea.repository.impl;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.domain.QCompany;
import kr.co.lionkorea.dto.request.FindCompaniesRequest;
import kr.co.lionkorea.dto.response.FindCompaniesResponse;
import kr.co.lionkorea.repository.CompanyQueryDslRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;

import java.util.List;

import static kr.co.lionkorea.domain.QCompany.*;

@RequiredArgsConstructor
public class CompanyQueryDslRepositoryImpl implements CompanyQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public PagedModel<FindCompaniesResponse> findCompanies(FindCompaniesRequest request, Pageable pageable) {
        List<FindCompaniesResponse> result = query.select(Projections.fields(FindCompaniesResponse.class,
                        company.id.as("companyId"),
                        company.companyName,
                        company.manager))
                .from(company)
                .orderBy(company.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int total = query.select(Expressions.constant(1))
                .from(company)
                .fetch().size();

        return new PagedModel<>(new PageImpl<>(result, pageable, total));
    }
}

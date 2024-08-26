package kr.co.lionkorea.repository.impl;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.lionkorea.domain.QMenu;
import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.enums.Role;
import kr.co.lionkorea.repository.MenuQueryDslRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static kr.co.lionkorea.domain.QMenu.menu;
import static kr.co.lionkorea.domain.QRoleMenu.roleMenu;
import static kr.co.lionkorea.domain.QRoles.roles;

@RequiredArgsConstructor
public class MenuQueryDslRepositoryImpl implements MenuQueryDslRepository {

    private final JPAQueryFactory query;

    @Override
    public List<FindMenuResponse> findMenusByRoleNames(Set<Role> roleNames) {
        return query
                .select(Projections.constructor(FindMenuResponse.class,
                        menu.id, menu.menuName, menu.menuLink, menu.menuIkon, menu.parentMenu.id))
                .from(menu)
                .innerJoin(roleMenu).on(menu.id.eq(roleMenu.menu.id))
                .innerJoin(roles).on(roleMenu.roles.id.eq(roles.id))
                .where(roles.roleName.in(roleNames))
                .orderBy(menu.orderSeq.asc())
                .fetch();
    }

}

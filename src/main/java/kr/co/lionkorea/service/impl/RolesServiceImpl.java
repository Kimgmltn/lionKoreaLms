package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Menu;
import kr.co.lionkorea.domain.Roles;
import kr.co.lionkorea.dto.request.MatchRolesAndMenusRequest;
import kr.co.lionkorea.dto.response.MatchRolesAndMenusResponse;
import kr.co.lionkorea.exception.RolesException;
import kr.co.lionkorea.repository.RolesRepository;
import kr.co.lionkorea.service.RolesService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RolesServiceImpl implements RolesService {

    private final RolesRepository rolesRepository;

    @Override
    @Transactional
    public MatchRolesAndMenusResponse matchRolesAndMenus(MatchRolesAndMenusRequest request){
        Roles roles = rolesRepository.findById(request.getRoleId()).orElseThrow(() -> new RolesException("존재하지 않는 권한입니다."));
        List<Menu> menus = request.getMenuIds().stream().map(Menu::new).collect(Collectors.toList());
        roles.addMenus(menus);
        rolesRepository.save(roles);

        return new MatchRolesAndMenusResponse("저장되었습니다.");
    }
}

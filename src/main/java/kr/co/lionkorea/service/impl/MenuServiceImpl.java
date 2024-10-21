package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Menu;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.SaveMenuRequest;
import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.dto.response.SaveMenuResponse;
import kr.co.lionkorea.exception.MenuException;
import kr.co.lionkorea.repository.MenuRepository;
import kr.co.lionkorea.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    @Transactional
    public SaveMenuResponse saveMenu(SaveMenuRequest request){

        if (request.getParentMenuId() == null) {
            Menu save = menuRepository.save(Menu.dtoToEntity(request));
            return new SaveMenuResponse(save.getId(), request.getMenuName() + " 메뉴가 생성되었습니다.");
        }else{
            Menu parentMenu = menuRepository.findById(request.getParentMenuId()).orElseThrow(() -> new MenuException("존재하지 않는 메뉴입니다."));
            Menu childMenu = Menu.dtoToEntity(request);
            parentMenu.addChildMenu(childMenu);
            Menu save = menuRepository.save(parentMenu);
            return new SaveMenuResponse(save.getId(), request.getMenuName() + " 메뉴가 생성되었습니다.");
        }
    }

    @Override
    public List<FindMenuResponse> findMenuByAuth(CustomUserDetails userDetails){
        List<FindMenuResponse> menus = menuRepository.findMenusByRoleNames(userDetails.getRoles());

        Map<Long, FindMenuResponse> menuMap = menus.stream().collect(Collectors.toMap(FindMenuResponse::getMenuId, m -> m));

        // childMenu를 parentMenu에 넣기
        for (FindMenuResponse m : menus) {
            if (m.getParentMenuId() != null) {
                FindMenuResponse parentMenu = menuMap.get(m.getParentMenuId());
                if (parentMenu != null) {
                    if (parentMenu.getChildMenu() == null) {
                        parentMenu.setChildMenu(new ArrayList<>());
                    }
                    parentMenu.getChildMenu().add(m);
                }
            }
        }

        return menus.stream().filter(m -> m.getParentMenuId() == null).collect(Collectors.toList());
    }

}

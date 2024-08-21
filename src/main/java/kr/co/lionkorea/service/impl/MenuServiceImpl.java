package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Menu;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.SaveMenuRequest;
import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.dto.response.SaveMenuResponse;
import kr.co.lionkorea.repository.MenuRepository;
import kr.co.lionkorea.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;

    @Override
    public SaveMenuResponse saveMenu(SaveMenuRequest request){
        menuRepository.save(Menu.dtoToEntity(request));
        return new SaveMenuResponse(request.getMenuName() + " 메뉴가 생성되었습니다");
    }

    @Override
    public List<FindMenuResponse> findMenuByAuth(CustomUserDetails userDetails){

        return menuRepository.findMenusByRoleNames(userDetails.getRoles());
    }

}

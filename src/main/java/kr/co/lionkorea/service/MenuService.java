package kr.co.lionkorea.service;

import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.SaveMenuRequest;
import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.dto.response.SaveMenuResponse;

import java.util.List;

public interface MenuService {
    SaveMenuResponse saveMenu(SaveMenuRequest request);

    List<FindMenuResponse> findMenuByAuth(CustomUserDetails userDetails);
}

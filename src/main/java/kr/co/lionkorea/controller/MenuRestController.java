package kr.co.lionkorea.controller;

import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.response.FindMenuResponse;
import kr.co.lionkorea.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/menu")
public class MenuRestController {

    private final MenuService menuService;

    @GetMapping("/my")
    public ResponseEntity<List<FindMenuResponse>> findAllMenuByAuth(@AuthenticationPrincipal CustomUserDetails userDetails){
        return ResponseEntity.ok(menuService.findMenuByAuth(userDetails));
    }
}

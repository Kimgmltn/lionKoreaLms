package kr.co.lionkorea.controller;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;
import kr.co.lionkorea.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Account> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization") String token){
        if (token != null && token.startsWith("Bearer ")) {
            String tokenParse = token.substring(7); // 'Bearer ' 이후의 JWT 토큰
            // 토큰을 블랙리스트에 추가하는 로직
            authService.addTokenToBlacklist(tokenParse); // 블랙리스트 서비스 호출
        }
        return ResponseEntity.ok().build(); // 로그아웃 응답
    }
}

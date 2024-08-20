package kr.co.lionkorea.controller;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;
import kr.co.lionkorea.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/login")
@Slf4j
public class LoginRestController {

    private final LoginService loginService;

    @PostMapping("/")
    public ResponseEntity<Account> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(loginService.login(request));
    }
}

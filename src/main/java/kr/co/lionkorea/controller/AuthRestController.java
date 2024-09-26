package kr.co.lionkorea.controller;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.domain.RefreshEntity;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.request.LoginRequest;
import kr.co.lionkorea.jwt.JwtUtil;
import kr.co.lionkorea.repository.RefreshRepository;
import kr.co.lionkorea.service.AuthService;
import kr.co.lionkorea.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Slf4j
public class AuthRestController {

    private final AuthService authService;
    private final JwtUtil jwtUtil;
    private final RefreshRepository refreshRepository;

//    @PostMapping("/login")
//    public ResponseEntity<Account> login(@RequestBody LoginRequest request){
//        return ResponseEntity.ok(authService.login(request));
//    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader(value = "Authorization") String token){
        if (token != null && token.startsWith("Bearer ")) {
            String tokenParse = token.substring(7); // 'Bearer ' 이후의 JWT 토큰
            // 토큰을 블랙리스트에 추가하는 로직
            authService.addTokenToBlacklist(tokenParse); // 블랙리스트 서비스 호출
        }
        return ResponseEntity.ok().build(); // 로그아웃 응답
    }

    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
            }
        }

        if (refresh == null) {
            return new ResponseEntity<>("refresh token null", HttpStatus.BAD_REQUEST);
        }

        try {
            jwtUtil.isExpire(refresh);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("access token expire", HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인
        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        // DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String newAccess = jwtUtil.createJwt("access", customUserDetails);
        String newRefresh = jwtUtil.createJwt("refresh", customUserDetails);

        // refresh 토큰 저장 DB에 기존의 Refresh 토큰 삭제 후 새 Refresh 토큰 저장
        refreshRepository.deleteByRefresh(refresh);

        RefreshEntity refreshEntity = new RefreshEntity(customUserDetails.getUsername(), refresh);
        refreshRepository.save(refreshEntity);

        response.setHeader("access", newAccess);
        response.addCookie(CommonUtils.createCookie("refresh", newRefresh));
        return ResponseEntity.ok().build();
    }
}

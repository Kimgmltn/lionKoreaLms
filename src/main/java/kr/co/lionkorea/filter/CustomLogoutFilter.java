package kr.co.lionkorea.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.jwt.JwtUtil;
import kr.co.lionkorea.service.RedisService;
import kr.co.lionkorea.utils.CommonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class CustomLogoutFilter extends GenericFilterBean {

    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        this.doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    private void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 토큰 검증
        String requestUrl = request.getRequestURI();
        if (!requestUrl.matches("^\\/logout$")) {
            filterChain.doFilter(request, response);
            return;
        }
        String requestMethod = request.getMethod();
        if (!requestMethod.equals("POST")) {
            filterChain.doFilter(request, response);
            return;
        }

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if ("refresh".equals(cookie.getName())) {
                refresh = cookie.getValue();
                log.info("refresh token in LogoutFilter: {}", refresh);
                break;
            }
        }

        if(jwtUtil.isExpire(refresh)){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        if (!"refresh".equals(jwtUtil.getCategory(refresh))) {
            log.info("LogoutFilter : there is not refresh");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        Long memberId = jwtUtil.getMemberId(refresh);
        if (!redisService.hasKey(memberId)) {
            log.info("LogoutFilter : there is no memberId");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        // 토큰 검증 끝
        
        // 로그아웃
        log.info("Logout begin");
        redisService.deleteKey(memberId);
        Cookie newCookie = CommonUtils.createCookie("refresh", null, 0);

        response.addCookie(newCookie);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}

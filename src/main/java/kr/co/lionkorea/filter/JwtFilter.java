package kr.co.lionkorea.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(requestURI.equals("/api/login")){
            filterChain.doFilter(request, response);
            return;
        }
        if(requestURI.startsWith("/api/")){
            String authorization = request.getHeader("Authorization");
            if(authorization == null || !authorization.startsWith("Bearer ")){
                log.info("Invalid or missing token");
                filterChain.doFilter(request, response);

                return;
            }
            // Bearer 접두사 제거후 토큰 획득
            String token = authorization.split(" ")[1];
            try {
                if (jwtUtil.isExpire(token)) {
                    log.info("token expire");
                    filterChain.doFilter(request, response);
                    return;
                }
            } catch (Exception e) {
                response.sendRedirect("/login");
                return;
            }


            // 토큰에서 username과 role 획득하여 securityContext에 저장
            MemberDetails memberDetails = MemberDetails.builder()
                    .loginId(jwtUtil.getLoginId(token))
                    .memberId(jwtUtil.getMemberId(token))
                    .memberName(jwtUtil.getMemberName(token))
                    .roles(jwtUtil.getRoles(token))
                    .password(null)
                    .build();
            CustomUserDetails customUserDetails = new CustomUserDetails(memberDetails);
            // 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }


        filterChain.doFilter(request, response);
    }
}

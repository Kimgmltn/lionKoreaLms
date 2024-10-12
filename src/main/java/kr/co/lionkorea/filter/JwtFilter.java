package kr.co.lionkorea.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.jwt.JwtUtil;
import kr.co.lionkorea.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@RequiredArgsConstructor
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthService authService;

//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
//        String requestURI = request.getRequestURI();
//
//        if(requestURI.equals("/api/auth/login")){
//            filterChain.doFilter(request, response);
//            return;
//        }
//        if(requestURI.startsWith("/api/")){
//            String authorization = request.getHeader("Authorization");
//            if(authorization == null || !authorization.startsWith("Bearer ")){
//                log.info("Invalid or missing token");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
//
//                return;
//            }
//            // Bearer 접두사 제거후 토큰 획득
//            String token = authorization.split(" ")[1];
//
//            if (authService.isTokenBlackList(token)) {
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
//                return;
//            }
//
//            try {
//                if (jwtUtil.isExpire(token)) {
//                    // TODO: 토큰 만료시 refresh토큰을 이용하여 재발급 받도록 처리
//                    log.info("Token is expire");
//                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is expire");
//                    return;
//                }
//            } catch (Exception e) {
//                log.info("Exception during tokenExpire check");
//                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Exception during tokenExpire check");
//                return;
//            }
//
//
//            // 토큰에서 username과 role 획득하여 securityContext에 저장
//            MemberDetails memberDetails = MemberDetails.builder()
//                    .loginId(jwtUtil.getLoginId(token))
//                    .memberId(jwtUtil.getMemberId(token))
//                    .memberName(jwtUtil.getMemberName(token))
//                    .roles(jwtUtil.getRoles(token))
//                    .password(null)
//                    .build();
//            CustomUserDetails customUserDetails = new CustomUserDetails(memberDetails);
//            // 시큐리티 인증 토큰 생성
//            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//            // 세션에 사용자 등록
//            SecurityContextHolder.getContext().setAuthentication(authToken);
//        }
//
//
//        filterChain.doFilter(request, response);
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();

        if(requestURI.equals("/api/auth/login")){
            log.info("/api/auth/login request");
            filterChain.doFilter(request, response);
            return;
        }

        if(requestURI.equals("/api/auth/reissue")){
            log.info("/api/auth/reissue request");
            filterChain.doFilter(request, response);
            return;
        }

        if(requestURI.startsWith("/api/")){
            String accessToken = request.getHeader("access");
            if(accessToken == null){
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token is blacklisted");
                return;
            }

            // 만료 확인
            if (jwtUtil.isExpire(accessToken)) {

                log.info("access token expire");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            String category = jwtUtil.getCategory(accessToken);
            if (!category.equals("access")) {

                log.info("invalid access token");

                PrintWriter writer = response.getWriter();
                writer.print("invalid access token");

                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

//            // 토큰에서 username과 role 획득하여 securityContext에 저장
            MemberDetails memberDetails = MemberDetails.builder()
                    .loginId(jwtUtil.getLoginId(accessToken))
                    .memberId(jwtUtil.getMemberId(accessToken))
                    .memberName(jwtUtil.getMemberName(accessToken))
                    .roles(jwtUtil.getRoles(accessToken))
                    .password(null)
                    .build();
            CustomUserDetails customUserDetails = new CustomUserDetails(memberDetails);
//            // 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
//            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }


        filterChain.doFilter(request, response);
    }
}

package kr.co.lionkorea.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.jwt.JwtUtil;
import kr.co.lionkorea.service.RedisService;
import kr.co.lionkorea.utils.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Map;

@Slf4j
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final RedisService redisService;

    public LoginFilter(AuthenticationManager authenticationManager, JwtUtil jwtUtil, RedisService redisService) {
        this.objectMapper = new ObjectMapper();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.redisService = redisService;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType())){
            try {
                Map<String, String> credentials = objectMapper.readValue(request.getInputStream(), Map.class);
                String loginId = credentials.get("loginId");
                String password = credentials.get("password");

                UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(loginId, password);
//                setDetails(request, authRequest);
                return authenticationManager.authenticate(authRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return super.attemptAuthentication(request, response);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        CustomUserDetails customUserDetails = (CustomUserDetails) authResult.getPrincipal();

//        String loginId = customUserDetails.getUsername();
//
//        List<Role> roles = new ArrayList<>();
//        Collection<? extends GrantedAuthority> authorities =
//                customUserDetails.getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            roles.add(Role.valueOf(authority.getAuthority()));
//        }

        String access = jwtUtil.createJwt("access",customUserDetails);
        String refresh = jwtUtil.createJwt("refresh", customUserDetails);
        log.info("access token : {}", access);
        log.info("refresh token : {}", refresh);

        redisService.saveRefreshToken(customUserDetails.getMemberId(), refresh);

//        response.addHeader("Authorization", "Bearer " + token);
//        super.successfulAuthentication(request, response, chain, authResult);
        response.setHeader("access", access);
        response.addCookie(CommonUtils.createCookie("refresh", refresh, 24*60*60));
        response.setStatus(HttpStatus.OK.value());

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        super.unsuccessfulAuthentication(request, response, failed);
    }
}

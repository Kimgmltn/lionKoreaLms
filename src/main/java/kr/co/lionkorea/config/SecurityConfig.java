package kr.co.lionkorea.config;

import jakarta.servlet.http.HttpServletResponse;
import kr.co.lionkorea.filter.CustomLogoutFilter;
import kr.co.lionkorea.filter.JwtFilter;
import kr.co.lionkorea.filter.LoginFilter;
import kr.co.lionkorea.jwt.JwtUtil;
import kr.co.lionkorea.service.AuthService;
import kr.co.lionkorea.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;
    private final AuthService authService;
    private final RedisService redisService;

    // static file에 대한 접근은 security를 확인하지 않음
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                // 필터 체인에서 제외할 URL 설정
                .requestMatchers("/js/**", "/css/**");
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, redisService);
        loginFilter.setFilterProcessesUrl("/api/auth/login");
        http    // 권한별 API 접근 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/api/auth/login", "/api/auth/reissue", "/api/members/*/valid", "/api/members/accounts/*/password").permitAll()
                        .requestMatchers("/api/members/**", "/api/files/**").hasAnyRole("ADMIN", "SUPER_ADMIN")
                        .requestMatchers("/api/**").authenticated()
//                        .requestMatchers("/login","/js/**", "/css/**", "/img/**", "/h2-console/**", "/").permitAll()
                        .anyRequest().permitAll())
                // 로그인 관련 설정
                // jwt에서는 form 로그인 방식이 아니기 때문에 disable 처리
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                /*.formLogin((formLogin -> formLogin
                        .loginPage("/login") // 로그인 페이지 URL
                        .usernameParameter("loginId") // 로그인 ID 파라미터값
                        .passwordParameter("password") // 로그인 PW 파라미터값
                        .loginProcessingUrl("/api/login") // 로그인 처리하는 URL
                        .successForwardUrl("/") // 로그인 성공시 보내는 URL
                        .permitAll()))*/
                // csrf 설정 
                .csrf(AbstractHttpConfigurer::disable)
                // X-Frame-Options 설정. h2 console이 X-Frame을 사용하기 때문에 넣음.
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                // 다중 로그인 설정.
                .sessionManagement((auth)->auth
                        // jwt는 stateless 상태이므로 해당 정책 추가
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        .sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId)
                        .maximumSessions(1) // 동시 접속 가능 숫자
                        .maxSessionsPreventsLogin(false)) // false시 이전 로그인건이 로그아웃
                // json 처리를 위한 필터 추가
                .addFilterBefore(new JwtFilter(jwtUtil, authService), LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, redisService), LogoutFilter.class)
                .rememberMe(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(((request, response, authException) -> {
                            log.error("FilterChain 중 인증 관련 에러 발생 message : {}", authException.getMessage());
                            log.error("FilterChain 중 인증 관련 에러 발생 trace : {}", Arrays.toString(authException.getStackTrace()));
                            // 인증 실패시 401 error 반환
                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
                        }))
                        .accessDeniedHandler(((request, response, accessDeniedException) -> {
                            // 권한이 없는 경우 403 error 반환
                            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Forbidden");
                        }))
                );

        return http.build();
    }
}

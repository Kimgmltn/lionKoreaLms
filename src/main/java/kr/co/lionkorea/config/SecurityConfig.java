package kr.co.lionkorea.config;

import jakarta.servlet.http.HttpServletRequest;
import kr.co.lionkorea.filter.JwtFilter;
import kr.co.lionkorea.filter.LoginFilter;
import kr.co.lionkorea.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    private final JwtUtil jwtUtil;

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
        LoginFilter loginFilter = new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil);
        loginFilter.setFilterProcessesUrl("/api/login");
        http    // 권한별 API 접근 설정
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login","/js/**", "/css/**", "/img/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
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
                .addFilterBefore(new JwtFilter(jwtUtil), LoginFilter.class)
                .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
                .rememberMe(Customizer.withDefaults())
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(((request, response, authException) -> {
                            response.sendRedirect("/login");
                        })));

        return http.build();
    }
}

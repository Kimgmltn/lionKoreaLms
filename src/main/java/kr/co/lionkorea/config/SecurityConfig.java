package kr.co.lionkorea.config;

import kr.co.lionkorea.filter.JsonAuthCustomFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        JsonAuthCustomFilter jsonAuthCustomFilter = new JsonAuthCustomFilter(authenticationManager);
        jsonAuthCustomFilter.setFilterProcessesUrl("/api/login");
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/login","/js/**", "/css/**", "/img/**", "/h2-console/**").permitAll()
                        .anyRequest().authenticated())
                .formLogin((formLogin -> formLogin.loginPage("/login")
                        .usernameParameter("loginId")
                        .passwordParameter("password")
                        .loginProcessingUrl("/api/login")
//                        .successForwardUrl("/")
                        .permitAll()))
                .csrf(AbstractHttpConfigurer::disable)
                // X-Frame-Options 설정. h2 console이 X-Frame을 사용하기 때문에 넣음.
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .addFilterBefore(jsonAuthCustomFilter, UsernamePasswordAuthenticationFilter.class)
                .rememberMe(Customizer.withDefaults());

        return http.build();
    }
}

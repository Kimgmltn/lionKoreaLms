package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.dto.CustomUserDetails;
import kr.co.lionkorea.dto.MemberDetails;
import kr.co.lionkorea.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberService memberService;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        log.info("security 로그인 시도 : {}", loginId);
        MemberDetails memberDetails = memberService.findUserDetails(loginId);

        return new CustomUserDetails(memberDetails);
    }
}

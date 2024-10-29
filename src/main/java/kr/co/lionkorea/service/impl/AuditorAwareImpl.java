package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.dto.CustomUserDetails;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuditorAwareImpl implements AuditorAware<Long> {

    @Override
    public Optional<Long> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null || !authentication.isAuthenticated()){
            return Optional.empty();
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof CustomUserDetails) {
            return Optional.of(((CustomUserDetails) principal).getMemberId());
        }else{
            return Optional.empty();
        }
    }
}

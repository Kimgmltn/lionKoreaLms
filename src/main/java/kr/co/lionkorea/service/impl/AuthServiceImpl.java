package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;
import kr.co.lionkorea.repository.AccountRepository;
import kr.co.lionkorea.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final AccountRepository accountRepository;
    private final Set<String> blackList = new HashSet<>();

    @Override
    public Account login(LoginRequest request) {
       return accountRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword());
    }

    @Override
    public void addTokenToBlacklist(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String tokenParse = token.split(" ")[1]; // 'Bearer ' 이후의 JWT 토큰
            blackList.add(tokenParse);
        }
    }

    @Override
    public boolean isTokenBlackList(String token) {
        return blackList.contains(token);
    }
}

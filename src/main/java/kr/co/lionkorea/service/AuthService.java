package kr.co.lionkorea.service;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;

public interface AuthService {
    Account login(LoginRequest request);

    void addTokenToBlacklist(String token);

    boolean isTokenBlackList(String token);
}

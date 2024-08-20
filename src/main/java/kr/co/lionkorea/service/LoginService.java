package kr.co.lionkorea.service;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;

public interface LoginService {
    Account login(LoginRequest request);
}

package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.domain.Account;
import kr.co.lionkorea.dto.request.LoginRequest;
import kr.co.lionkorea.repository.AccountRepository;
import kr.co.lionkorea.service.LoginService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LoginServiceImpl implements LoginService {

    private AccountRepository accountRepository;

    @Override
    public Account login(LoginRequest request) {
       log.info("데이터 들어옴");
       return accountRepository.findByLoginIdAndPassword(request.getLoginId(), request.getPassword());
    }
}

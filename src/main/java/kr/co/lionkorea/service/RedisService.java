package kr.co.lionkorea.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface RedisService {

    void saveRefreshToken(Long key, String refreshToken);

    void save(String key, String value, Long time);

    String getValue(Long key);

    String getValue(String key);

    Boolean hasKey(Long key);
    Boolean hasKey(String key);

    void deleteKey(Long key);

    Boolean isFirstRequest(Long key);
}

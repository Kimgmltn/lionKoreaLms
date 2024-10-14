package kr.co.lionkorea.service;

public interface RedisService {

    void saveRefreshToken(Long key, String refreshToken);

    String getValue(Long key);

    Boolean hasKey(Long key);

    void deleteKey(Long key);

    Boolean isFirstRequest(Long key);
}

package kr.co.lionkorea.service;

public interface RedisService {

    void saveRefreshToken(String key, String refreshToken);

    String getValue(String key);

    Boolean hasKey(String key);

    void deleteKey(String key);

}

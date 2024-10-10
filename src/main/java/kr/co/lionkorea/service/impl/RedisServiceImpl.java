package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void saveRefreshToken(String key, String refreshToken) {
        redisTemplate.opsForValue().set(key, refreshToken);
        redisTemplate.expire(key, 8L, TimeUnit.HOURS); // 8시간 지나면 삭제
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void deleteKey(String key) {
        redisTemplate.delete(key);
    }
}

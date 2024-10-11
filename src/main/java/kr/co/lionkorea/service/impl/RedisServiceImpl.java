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
    public void saveRefreshToken(Long key, String refreshToken) {
        String stringKey = stringKey(key);
        redisTemplate.opsForValue().set(stringKey, refreshToken);
        redisTemplate.expire(stringKey, 8L, TimeUnit.HOURS); // 8시간 지나면 삭제
    }

    @Override
    public String getValue(Long key) {
        return redisTemplate.opsForValue().get(stringKey(key));
    }

    @Override
    public Boolean hasKey(Long key) {
        return redisTemplate.hasKey(stringKey(key));
    }

    @Override
    public void deleteKey(Long key) {
        redisTemplate.delete(stringKey(key));
    }

    private String stringKey(Long key){
        return String.valueOf(key);
    }
}

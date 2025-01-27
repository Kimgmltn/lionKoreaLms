package kr.co.lionkorea.service.impl;

import kr.co.lionkorea.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    @Transactional
    @Async
    public void saveRefreshToken(Long key, String refreshToken) {
        String stringKey = longToStringKey(key);
        redisTemplate.opsForValue().set(stringKey, refreshToken);
        redisTemplate.expire(stringKey, 8L, TimeUnit.HOURS); // 8시간 지나면 삭제
    }

    @Override
    @Transactional
    @Async
    public void save(String key, String value, Long time) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, time, TimeUnit.MINUTES);
    }

    @Override
    public String getValue(Long key) {
        return redisTemplate.opsForValue().get(longToStringKey(key));
    }

    @Override
    public String getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Boolean hasKey(Long key) {
        return redisTemplate.hasKey(longToStringKey(key));
    }

    @Override
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    @Override
    public void deleteKey(Long key) {
        redisTemplate.delete(longToStringKey(key));
    }

    @Override
    public Boolean isFirstRequest(Long key) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        return Boolean.TRUE.equals(ops.setIfAbsent("lock:" + longToStringKey(key), String.valueOf(true), 10, TimeUnit.SECONDS));
    }

    private String longToStringKey(Long key){
        return String.valueOf(key);
    }
}

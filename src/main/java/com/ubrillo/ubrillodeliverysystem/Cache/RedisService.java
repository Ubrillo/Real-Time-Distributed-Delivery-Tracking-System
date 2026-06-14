package com.ubrillo.ubrillodeliverysystem.Cache;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    private static final String KEY_PREFIX = "order:";

    public RedisService(RedisTemplate<String, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public void updateState(OrderState state){
        redisTemplate.opsForValue().set(KEY_PREFIX + state.requestId(), state);
    }

    public  OrderState getState(String requestId){
        return (OrderState)  redisTemplate.opsForValue().get(KEY_PREFIX+requestId);
    }

    public void removeState(String requestId){
        redisTemplate.delete(KEY_PREFIX + requestId);
    }

}

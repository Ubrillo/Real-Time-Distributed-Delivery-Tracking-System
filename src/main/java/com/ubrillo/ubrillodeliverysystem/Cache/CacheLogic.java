package com.ubrillo.ubrillodeliverysystem.Cache;

import org.modelmapper.ModelMapper;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CacheLogic {

    private final Map<String, OrderState> stateMap =
            new ConcurrentHashMap<>();

    private RedisTemplate<String, OrderState> redisTemplate;

    private final boolean REDIS_CACHE = false;

    private static final String KEY_PREFI = "order:";


    public CacheLogic(RedisTemplate<String, OrderState> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public OrderState getState(String requestId){
        if(!REDIS_CACHE){
            return stateMap.get(requestId);
        }
        return (OrderState)  redisTemplate.opsForValue().get(KEY_PREFI+requestId);
    }

    public void updateState(OrderState state) {
        if (!REDIS_CACHE){
            OrderState oldState = stateMap.get(state.requestId());
            OrderState updatedState = updateHistory(oldState, state);
            stateMap.put(
                    updatedState.requestId(),
                    updatedState
            );
            return ;
        }
        redisTemplate.opsForValue().set(KEY_PREFI + state.requestId(), state);
    }

    public Map<String, OrderState> getAllStates() {
        return stateMap;
    }

    public void removeState(String requestId) {
        if (!REDIS_CACHE){
            stateMap.remove(requestId);
            return ;
        }
        redisTemplate.delete(KEY_PREFI + requestId);

    }

    private OrderState updateHistory(OrderState oldState, OrderState newState) {
        String history = "";

        if (oldState != null){
            history = oldState.history() + newState.history();
        }
        ModelMapper mapper = new ModelMapper();
        return mapper.map(oldState, OrderState.class);
    }
}
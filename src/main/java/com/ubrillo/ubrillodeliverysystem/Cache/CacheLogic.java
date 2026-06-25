package com.ubrillo.ubrillodeliverysystem.Cache;

import com.ubrillo.ubrillodeliverysystem.Logic.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service class responsible for managing cached order states.
 * <p>
 * This class supports both in-memory caching using {@link ConcurrentHashMap}
 * and Redis-based caching using {@link RedisTemplate}.
 */
@Service
public class CacheLogic {

    /**
     * In-memory storage for order states, mapped by request ID.
     */
    private final Map<String, OrderState> stateMap =
            new ConcurrentHashMap<>();

    /**
     * Redis template used for storing and retrieving order states from Redis.
     */
    private RedisTemplate<String, OrderState> redisTemplate;

    /**
     * Flag used to determine whether Redis caching should be used.
     * When false, the in-memory cache is used instead.
     */
    private final boolean REDIS_CACHE = false;

    /**
     * Prefix used when storing order states in Redis.
     */
    private static final String KEY_PREFI = "order:";

    /**
     * Mapper used to convert order state objects.
     */
    @Autowired
    private ObjectMapper mapper;


    /**
     * Creates a new CacheLogic instance with the required Redis template.
     *
     * @param redisTemplate Redis template used for order state cache operations.
     */
    public CacheLogic(RedisTemplate<String, OrderState> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Retrieves the cached order state for the given request ID.
     *
     * @param requestId Unique identifier of the request.
     * @return The matching {@link OrderState}, or null if no state exists.
     */
    public OrderState getState(String requestId){
        if(!REDIS_CACHE){
            return stateMap.get(requestId);
        }
        return (OrderState)  redisTemplate.opsForValue().get(KEY_PREFI+requestId);
    }

    /**
     * Updates the cached order state.
     * <p>
     * If Redis caching is disabled, the state is stored in the in-memory map.
     * Otherwise, the state is stored in Redis.
     *
     * @param state Order state to be updated in the cache.
     */
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

    /**
     * Retrieves all order states stored in the in-memory cache.
     *
     * @return Map containing all cached order states.
     */
    public Map<String, OrderState> getAllStates() {
        return stateMap;
    }

    /**
     * Removes an order state from the cache using its request ID.
     *
     * @param requestId Unique identifier of the request to remove.
     */
    public void removeState(String requestId) {
        if (!REDIS_CACHE){
            stateMap.remove(requestId);
            return ;
        }
        redisTemplate.delete(KEY_PREFI + requestId);

    }

    /**
     * Updates the history of an order state using the previous and new state.
     *
     * @param oldState Previous order state stored in the cache.
     * @param newState New order state to be cached.
     * @return Updated {@link OrderState} instance.
     */
    private OrderState updateHistory(OrderState oldState, OrderState newState) {
        String history = "";

        if (oldState != null){
            history = oldState.history() + newState.history();
        }
//        ModelMapper mapper = new ModelMapper();
//        return mapper.map(oldState, OrderState.class);

        return mapper.orderStateToOrderState(newState);
    }
}

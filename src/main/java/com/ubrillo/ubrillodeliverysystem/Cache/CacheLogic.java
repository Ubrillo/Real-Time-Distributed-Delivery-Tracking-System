package com.ubrillo.ubrillodeliverysystem.Cache;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheLogic implements Cache {

    private final Map<String, OrderState> stateMap =
            new ConcurrentHashMap<>();

    public CacheLogic(){}

    public OrderState getState(String requestId){
        return stateMap.get(requestId);
    }

    public void updateState(OrderState state) {
        OrderState oldState = stateMap.get(state.requestId());
        OrderState updatedState = updateHistory(oldState, state);
        stateMap.put(
                updatedState.requestId(),
                updatedState
        );
    }

    public Map<String, OrderState> getAllStates() {
        return stateMap;
    }

    public void removeState(String requestId) {
        stateMap.remove(requestId);
    }

    private OrderState  updateHistory(OrderState oldState, OrderState newState) {
        String history = "";

        if (oldState != null){
            history = oldState.history() + newState.history();
        }
        return new OrderState(
                newState.requestId(),
                newState.status(),
                newState.updatedAt(),
                newState.location(),
                newState.destination(),
                history
        );
    }
}

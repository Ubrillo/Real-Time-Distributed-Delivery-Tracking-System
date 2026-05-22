package com.ubrillo.ubrillodeliverysystem.StateManagement;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OrderStateStore {
    private final Map<String, OrderState> stateMap =
            new ConcurrentHashMap<>();

    public void updateState(OrderState state) {
        OrderState oldState = stateMap.get(state.requestId());
        OrderState updatedState = updateHistory(oldState, state);
        stateMap.put(
                updatedState.requestId(),
                updatedState
        );
    }

    public OrderState getState(String requestId) {
        return stateMap.get(requestId);
    }

    public Map<String, OrderState> getAllStates() {
        return stateMap;
    }

    public void removeState(String requestId) {
        stateMap.remove(requestId);
    }

    private OrderState  updateHistory(OrderState oldState, OrderState newState) {
        String history = oldState.history() + newState.history();
        return new OrderState(
                newState.requestId(),
                newState.status(),
                newState.updatedAt(),
                newState.location(),
                history
        );
    }
}
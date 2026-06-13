package com.ubrillo.ubrillodeliverysystem.Cache;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class OrderStateStore {

    private CacheLogic cacheLogic;

//    private final Map<String, OrderState> stateMap =
//            new ConcurrentHashMap<>();
//

    public void updateState(OrderState state) {
//        OrderState oldState = stateMap.get(state.requestId());
//        OrderState updatedState = updateHistory(oldState, state);
//        stateMap.put(
//                updatedState.requestId(),
//                updatedState
//        );

        cacheLogic.updateState(state);
    }


    public OrderState getState(String requestId) {
        //return stateMap.get(requestId);
        return cacheLogic.getState(requestId);
    }

    public Map<String, OrderState> getAllStates() {
        //return stateMap;
        return cacheLogic.getAllStates();
    }

    public void removeState(String requestId) {
        //stateMap.remove(requestId);
        cacheLogic.removeState(requestId);
    }
//
//    private OrderState  updateHistory(OrderState oldState, OrderState newState) {
////        String history = "";
////
////        if (oldState != null){
////            history = oldState.history() + newState.history();
////        }
////        return new OrderState(
////                newState.requestId(),
////                newState.status(),
////                newState.updatedAt(),
////                newState.location(),
////                newState.destination(),
////                history
////        );
//        return cacheLogic.updateHistory( oldState,  newState);
//    }
}
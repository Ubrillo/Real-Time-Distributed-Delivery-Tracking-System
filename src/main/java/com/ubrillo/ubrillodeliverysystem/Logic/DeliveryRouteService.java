package com.ubrillo.ubrillodeliverysystem.Logic;

import com.google.ortools.Loader;
import com.google.ortools.constraintsolver.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Service responsible for optimizing delivery routes using OR-Tools
 * and OSRM distance matrix API.
 */
@Service
public class DeliveryRouteService {

    private final RestClient restClient = RestClient.create();

    /**
     * Initializes native OR-Tools libraries.
     */
    public DeliveryRouteService(){
        Loader.loadNativeLibraries();
    }

    /**
     * Optimizes the delivery route using a distance/duration matrix.
     *
     * @param stops list of delivery stops
     * @return optimized route result with ordered stops and total duration
     */
    public RouteResult optimizeRoute(List<DeliveryStop> stops){

        long[][] durationMatrix = fetchOsrmDurationMatrix(stops);

        int vehicleCount = 1;
        int depotIndex = 0;

        RoutingIndexManager manager = new RoutingIndexManager(
                durationMatrix.length,
                vehicleCount,
                depotIndex
        );

        RoutingModel routing = new RoutingModel(manager);

        int transitCallbackIndex = routing.registerTransitCallback((fromIndex, toIndex)->{
            int fromNode = manager.indexToNode(fromIndex);
            int toNode = manager.indexToNode(toIndex);
            return durationMatrix[fromNode][toNode];
        });

        routing.setArcCostEvaluatorOfAllVehicles(transitCallbackIndex);

        RoutingSearchParameters searchParameters =
                main.defaultRoutingSearchParameters()
                        .toBuilder()
                        .setFirstSolutionStrategy(FirstSolutionStrategy.Value.PATH_CHEAPEST_ARC)
                        .setLocalSearchMetaheuristic(LocalSearchMetaheuristic.Value.GUIDED_LOCAL_SEARCH)
                        .setTimeLimit(com.google.protobuf.Duration.newBuilder().setSeconds(10).build())
                        .build();

        Assignment solution = routing.solveWithParameters(searchParameters);

        if (solution == null) {
            throw new IllegalStateException("No route found");
        }

        List<DeliveryStop> orderedStops = new ArrayList<>();
        long totalSeconds = 0;

        long index = routing.start(0);

        while (!routing.isEnd(index)) {
            int nodeIndex = manager.indexToNode(index);
            orderedStops.add(stops.get(nodeIndex));

            long previousIndex = index;
            index = solution.value(routing.nextVar(index));

            totalSeconds += routing.getArcCostForVehicle(previousIndex, index, 0);
        }

        orderedStops.add(stops.get(manager.indexToNode(index)));

        return new RouteResult(orderedStops, totalSeconds);
    }

    /**
     * Fetches a travel duration matrix from OSRM API for all delivery stops.
     *
     * @param stops list of delivery stops
     * @return matrix of travel durations between stops
     */
    private long[][] fetchOsrmDurationMatrix(List<DeliveryStop> stops) {

        String coordinates = stops.stream()
                .map(stop -> stop.lng() + "," + stop.lat())
                .reduce((a, b) -> a + ";" + b)
                .orElseThrow();

        String url = "https://router.project-osrm.org/table/v1/driving/"
                + coordinates
                + "?annotations=duration";

        OsrmTableResponse response = restClient.get()
                .uri(url)
                .header("Accept-Encoding", "identity")
                .retrieve()
                .body(OsrmTableResponse.class);

        if (response == null || !"Ok".equals(response.code())) {
            throw new IllegalStateException("OSRM matrix request failed");
        }

        List<List<Double>> durations = response.durations();
        long[][] matrix = new long[durations.size()][durations.size()];

        for (int i = 0; i < durations.size(); i++) {
            for (int j = 0; j < durations.get(i).size(); j++) {
                matrix[i][j] = Math.round(durations.get(i).get(j));
            }
        }

        return matrix;
    }
}
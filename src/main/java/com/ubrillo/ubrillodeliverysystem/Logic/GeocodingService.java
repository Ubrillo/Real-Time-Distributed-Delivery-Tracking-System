package com.ubrillo.ubrillodeliverysystem.Logic;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class GeocodingService {
    private final RestTemplate restTemplate;

    public Coordinate getCoordinates(String postcode){
        String url =
                "https://nominatim.openstreetmap.org/search?q="
                        + postcode
                        + "&format=json&limit=1";

        ResponseEntity<NominatimResponse[]> response =
                restTemplate.getForEntity(
                        url,
                        NominatimResponse[].class
                );

        var result = response.getBody()[0];

        return new Coordinate(
                Double.parseDouble(result.getLatitude()),
                Double.parseDouble(result.getLongitude())
        );
    }
}

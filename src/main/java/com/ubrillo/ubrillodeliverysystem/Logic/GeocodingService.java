package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class GeocodingService {

    private final RestTemplate restTemplate = new RestTemplate();

    public Coordinate getCoordinates(String postcode) {
        String url = UriComponentsBuilder
                .fromUriString("https://nominatim.openstreetmap.org/search")
                .queryParam("q", postcode)
                .queryParam("format", "json")
                .queryParam("limit", 1)
                .build()
                .toUriString();

        HttpHeaders headers = new HttpHeaders();
        headers.set(
                HttpHeaders.USER_AGENT,
                "ubrillo-delivery-system/1.0 (grandsonudoette005@gmail.com)"
        );

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<NominatimResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                entity,
                NominatimResponse[].class
        );

        NominatimResponse[] body = response.getBody();

        if (body == null || body.length == 0) {
            throw new IllegalArgumentException("No coordinates found for postcode: " + postcode);
        }

        NominatimResponse result = body[0];


        return new Coordinate(
                Double.parseDouble(result.getLatitude()),
                Double.parseDouble(result.getLongitude())
        );
    }
}
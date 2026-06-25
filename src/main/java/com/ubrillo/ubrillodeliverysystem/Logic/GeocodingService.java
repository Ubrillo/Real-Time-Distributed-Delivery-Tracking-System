package com.ubrillo.ubrillodeliverysystem.Logic;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service responsible for converting postcodes into geographic coordinates
 * using the OpenStreetMap Nominatim API, with caching and rate limiting.
 */
@Service
public class GeocodingService {

    private static final Duration MIN_REQUEST_INTERVAL = Duration.ofSeconds(1);

    private final RestTemplate restTemplate = new RestTemplate();
    private final Map<String, Coordinate> cache = new ConcurrentHashMap<>();

    private Instant lastRequestTime = Instant.EPOCH;

    /**
     * Retrieves coordinates for a given postcode, using cache when available.
     */
    public Coordinate getCoordinates(String postcode) {
        String normalizedPostcode = postcode.trim().toUpperCase();

        Coordinate cachedCoordinate = cache.get(normalizedPostcode);
        if (cachedCoordinate != null) {
            return cachedCoordinate;
        }

        synchronized (this) {
            cachedCoordinate = cache.get(normalizedPostcode);
            if (cachedCoordinate != null) {
                return cachedCoordinate;
            }

            waitForRateLimit();

            Coordinate coordinate = fetchCoordinates(normalizedPostcode);
            cache.put(normalizedPostcode, coordinate);
            lastRequestTime = Instant.now();

            return coordinate;
        }
    }

    /**
     * Calls the Nominatim API to fetch coordinates for a postcode.
     */
    private Coordinate fetchCoordinates(String postcode) {

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

        try {
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

        } catch (HttpClientErrorException.TooManyRequests exception) {
            throw new IllegalStateException(
                    "Nominatim rate limit reached. Please wait and try again.",
                    exception
            );
        }
    }

    /**
     * Enforces minimum delay between external API requests to respect rate limits.
     */
    private void waitForRateLimit() {
        Duration elapsed = Duration.between(lastRequestTime, Instant.now());
        Duration remaining = MIN_REQUEST_INTERVAL.minus(elapsed);

        if (!remaining.isNegative() && !remaining.isZero()) {
            try {
                Thread.sleep(remaining.toMillis());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for geocoding rate limit", exception);
            }
        }
    }
}


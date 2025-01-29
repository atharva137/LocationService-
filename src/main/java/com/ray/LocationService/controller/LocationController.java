package com.ray.LocationService.controller;

import com.ray.LocationService.bean.ErrorResponse;
import com.ray.LocationService.bean.LocationResponse;
import com.ray.LocationService.exception.RateLimitExceededException;
import com.ray.LocationService.service.LocationService;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import io.github.bucket4j.Bandwidth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.web.bind.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
public class LocationController {

    private final LocationService locationService;
    private final Bucket bucket;
    private static final Logger logger = LoggerFactory.getLogger(LocationController.class);

    @GetMapping(value = "api/v1/locationservice/search/location", produces = MediaType.APPLICATION_JSON_VALUE)

    @CrossOrigin
    @Retryable(
            value = { RateLimitExceededException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 10000, multiplier = 1.5)
    )
    public CompletableFuture<ResponseEntity<List<LocationResponse>>> getLocations(@RequestParam String query) {
        logger.debug(getClass().getName() + " Start getLocations for query " + query);
        try {
            // Rate limiting logic
            if (!tryConsume()) {
                return CompletableFuture.completedFuture(ResponseEntity.status(429).body(null)); // 429 Too Many Requests
            }

            // Fetch the locations asynchronously
            return locationService.searchLocation(query)
                    .thenApply(ResponseEntity::ok)
                    .exceptionally(ex -> {
                        logger.error("Error fetching location data for query: " + query, ex);
                        return ResponseEntity.status(500).body(null); // Internal Server Error
                    });

        } catch (NullPointerException ex) {
            logger.error("Null Pointer Exception for query: " + query, ex);
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(null)); // Handle null pointer
        } catch (Exception ex) {
            logger.error("Unexpected error occurred while fetching location for query: " + query, ex);
            return CompletableFuture.completedFuture(ResponseEntity.status(500).body(null)); // Handle other exceptions
        }
    }

    @Autowired
    public LocationController(LocationService locationService) {
        this.locationService = locationService;

        // Rate-limiting using Bucket4j (100 requests per minute, refills 1 token per second)
        Refill refill = Refill.intervally(1, Duration.ofSeconds(1));
        Bandwidth limit = Bandwidth.classic(1000, refill);
        this.bucket = Bucket4j.builder()
                .addLimit(limit)
                .build();
    }

    // Retry mechanism to attempt the request when rate-limited
    private boolean tryConsume() {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        if (!probe.isConsumed()) {
            logger.warn("Rate limit exceeded. Remaining capacity: " + probe.getRemainingTokens());
            throw new RateLimitExceededException("Rate limit exceeded");
        }
        return true;
    }

    // Fallback method for retry exhaustion (if rate-limited multiple times)
    @Recover
    public CompletableFuture<ResponseEntity<String>> recover(RateLimitExceededException ex) {
        logger.error("Rate limit exceeded for multiple attempts. Recovering with status 503.");
        return CompletableFuture.completedFuture(ResponseEntity.status(503).body("Service temporarily unavailable, please try again later."));
    }
}

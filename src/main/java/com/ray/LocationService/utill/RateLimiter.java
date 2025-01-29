package com.ray.LocationService.utill;

import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import org.springframework.stereotype.Component;
import io.github.bucket4j.Bucket4j;



import java.time.Duration;


@Component
public class RateLimiter {

    private final Bucket bucket;

    public RateLimiter() {
        // Setting rate limit to 1000 requests per minute
        this.bucket = Bucket4j.builder()
                .addLimit(io.github.bucket4j. Bandwidth.simple(10000, Duration.ofMinutes(1)))
                .build();
    }

    public boolean tryConsume() {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);
        return probe.isConsumed();
    }

    public long getRemainingTokens() {
        return bucket.getAvailableTokens();
    }
}

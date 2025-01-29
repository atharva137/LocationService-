package com.ray.LocationService.service.impl;

import com.ray.LocationService.bean.LocationResponse;
import com.ray.LocationService.exception.LocationServiceException;
import com.ray.LocationService.repository.GenericCache;
import com.ray.LocationService.service.LocationService;
import com.ray.LocationService.service.OlaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

@Service
public class LocationServiceImpl implements LocationService {


    private static final Logger logger = Logger.getLogger(LocationServiceImpl.class.getName());
    private final OlaService olaService;


    @Autowired
    public LocationServiceImpl(OlaService olaService) {
        this.olaService = olaService;

    }


    @Async("taskExecutor")
    @Override
    public CompletableFuture<List<LocationResponse>> searchLocation(String query) {

        if (query == null || query.trim().isEmpty()) {
            logger.warning("Invalid query: Query cannot be null or empty.");
            return CompletableFuture.failedFuture(new IllegalArgumentException("Query cannot be null or empty"));  // Return empty list if query is invalid
        }

        return olaService.searchLocations(query)
                .handle((locationResponseList, ex) -> {
                    if (ex != null) {
                        // Log the error
                        throw (new LocationServiceException("Failed to fetch location data"));
                    }
                    return (locationResponseList);
                });

    }


    private void handleApiError(String query, Throwable e) {
        // Log the error for auditing or debugging
        System.err.println("Error occurred while fetching location for query: " + query + ". Error: " + e.getMessage());
    }
}

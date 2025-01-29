package com.ray.LocationService.service;

import com.ray.LocationService.bean.LocationResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface OlaService {

    public CompletableFuture<List<LocationResponse>> searchLocations(String query);
}

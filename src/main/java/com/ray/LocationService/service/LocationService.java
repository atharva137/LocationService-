package com.ray.LocationService.service;

import com.ray.LocationService.bean.LocationResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface LocationService {

   public CompletableFuture<List<LocationResponse>> searchLocation(String query);
}

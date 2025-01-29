package com.ray.LocationService.service;

import com.ray.LocationService.bean.AddressDetailRequest;
import com.ray.LocationService.bean.AddressDetailResponse;

import java.util.concurrent.CompletableFuture;

public interface AddressService {

    public CompletableFuture<Long> saveAddressDetail(AddressDetailRequest addressDetailRequest);

    public CompletableFuture<AddressDetailResponse> getAddressDetail(Long addressId);
}

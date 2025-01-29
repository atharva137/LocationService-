package com.ray.LocationService.controller;

import com.ray.LocationService.bean.AddressDetailRequest;
import com.ray.LocationService.bean.AddressDetailResponse;
import com.ray.LocationService.exception.LocationServiceException;
import com.ray.LocationService.service.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/v1/locationservice/address")
public class AddressController {

    private static final Logger logger = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    /**
     * Save address details.
     *
     * @param addressDetailRequest the request payload containing address details
     * @return a CompletableFuture wrapping a ResponseEntity containing the saved address ID
     */
    @PostMapping
    public CompletableFuture<ResponseEntity<Long>> saveAddressDetail(@RequestBody AddressDetailRequest addressDetailRequest) {
        return addressService.saveAddressDetail(addressDetailRequest)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error occurred while saving address", ex);
                    throw new LocationServiceException("Error occurred while saving address", ex);
                });
    }

    /**
     * Get address details by ID.
     *
     * @param addressId the ID of the address
     * @return a CompletableFuture wrapping a ResponseEntity containing the address details
     */
    @GetMapping("/{addressId}")
    public CompletableFuture<ResponseEntity<AddressDetailResponse>> getAddressDetailByAddressId(@PathVariable Long addressId) {
        return addressService.getAddressDetail(addressId)
                .thenApply(ResponseEntity::ok)
                .exceptionally(ex -> {
                    logger.error("Error occurred while retrieving address details", ex);
                    throw new LocationServiceException("Error occurred while retrieving address details", ex);
                });
    }
}

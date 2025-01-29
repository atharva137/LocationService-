package com.ray.LocationService.service.impl;

import com.ray.LocationService.bean.AddressDetailRequest;
import com.ray.LocationService.bean.AddressDetailResponse;
import com.ray.LocationService.config.CaffeineCache;
import com.ray.LocationService.entity.AddressDetail;
import com.ray.LocationService.exception.LocationServiceException;
import com.ray.LocationService.exception.AddressNotFoundException;
import com.ray.LocationService.repository.AddressDetailRepository;
import com.ray.LocationService.service.AddressService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
public class AddressServiceImpl implements AddressService {

    private static final Logger logger = LoggerFactory.getLogger(AddressServiceImpl.class);
    private final AddressDetailRepository addressDetailRepository;
    private final CaffeineCache<String, AddressDetailResponse> addressCache;
    private final Executor taskExecutor;

    public AddressServiceImpl(AddressDetailRepository addressDetailRepository,
                              CaffeineCache<String, AddressDetailResponse> addressCache,
                              @Qualifier("taskExecutor") Executor taskExecutor) {
        this.addressDetailRepository = addressDetailRepository;
        this.addressCache = addressCache;
        this.taskExecutor = taskExecutor;
    }

    @Override
    public CompletableFuture<Long> saveAddressDetail(AddressDetailRequest addressDetailRequest) {
        logger.info("Initiating saveAddressDetail for placeId: {}", addressDetailRequest.getPlaceId());

        return CompletableFuture.supplyAsync(() -> addressDetailRepository.findByPlaceId(addressDetailRequest.getPlaceId()), taskExecutor)
                .thenComposeAsync(existingAddress -> handleAddressDetail(existingAddress, addressDetailRequest), taskExecutor)
                .handle((result, ex) -> {
                    if (ex != null) {
                        logger.error("Error in saveAddressDetail for placeId: {} - {}", addressDetailRequest.getPlaceId(), ex.getMessage());
                        throw new LocationServiceException("Failed to save address detail", ex);
                    }
                    logger.info("Successfully saved address detail for placeId: {}, addressId: {}", addressDetailRequest.getPlaceId(), result);
                    return result;
                });
    }

    @Override
    public CompletableFuture<AddressDetailResponse> getAddressDetail(Long addressId) {
        String cacheKey = "address_" + addressId;
        logger.info("Fetching address detail for addressId: {}", addressId);

        return CompletableFuture.supplyAsync(() -> getCachedOrDatabaseAddressDetail(cacheKey, addressId), taskExecutor)
                .handle((response, ex) -> {
                    if (ex != null) {
                        logger.error("Error in getAddressDetail for addressId: {} - {}", addressId, ex.getMessage());
                        throw new LocationServiceException("Failed to fetch address detail", ex);
                    }
                    logger.info("Successfully fetched address detail for addressId: {}", addressId);
                    return response;
                });
    }

    @Transactional
    private CompletableFuture<Long> handleAddressDetail(AddressDetail existingAddress, AddressDetailRequest addressDetailRequest) {
        if (existingAddress != null) {
            logger.info("Address already exists for placeId: {} with addressId: {}", addressDetailRequest.getPlaceId(), existingAddress.getAddressId());
            return CompletableFuture.completedFuture(existingAddress.getAddressId());
        }

        return CompletableFuture.supplyAsync(() -> {
            AddressDetail newAddressDetail = createAddressDetailEntity(addressDetailRequest);
            AddressDetail savedAddressDetail = addressDetailRepository.save(newAddressDetail);
            logger.info("New address saved for placeId: {}, addressId: {}", addressDetailRequest.getPlaceId(), savedAddressDetail.getAddressId());
            return savedAddressDetail.getAddressId();
        }, taskExecutor);
    }

    private AddressDetailResponse getCachedOrDatabaseAddressDetail(String cacheKey, Long addressId) {
        return Optional.ofNullable(addressCache.get(cacheKey))
                .orElseGet(() -> {
                    AddressDetailResponse response = fetchAndCacheAddressDetailFromDatabase(addressId, cacheKey);
                    if (response == null) {
                        logger.warn("Address detail not found in cache or database for addressId: {}", addressId);
                        throw new AddressNotFoundException("Address not found for ID: " + addressId);
                    }
                    return response;
                });
    }

    private AddressDetailResponse fetchAndCacheAddressDetailFromDatabase(Long addressId, String cacheKey) {
        return addressDetailRepository.findById(addressId)
                .map(this::convertToResponse)
                .map(response -> {
                    addressCache.put(cacheKey, response);
                    logger.info("Address detail cached for addressId: {}", addressId);
                    return response;
                })
                .orElse(null);
    }

    private AddressDetailResponse convertToResponse(AddressDetail addressDetail) {
        AddressDetailResponse response = new AddressDetailResponse();
        response.setAddressId(addressDetail.getAddressId());
        response.setCountry(addressDetail.getCountry());
        response.setDescription(addressDetail.getDescription());
        response.setLatitude(addressDetail.getLatitude());
        response.setMainText(addressDetail.getMainText());
        response.setSecondaryText(addressDetail.getSecondaryText());
        response.setLongitude(addressDetail.getLongitude());
        response.setPincode(addressDetail.getPincode());
        logger.debug("Converted AddressDetail to AddressDetailResponse for addressId: {}", addressDetail.getAddressId());
        return response;
    }

    private AddressDetail createAddressDetailEntity(AddressDetailRequest addressDetailRequest) {
        AddressDetail addressDetail = new AddressDetail();
        addressDetail.setDescription(addressDetailRequest.getDescription());
        addressDetail.setMainText(addressDetailRequest.getMainText());
        addressDetail.setLatitude(addressDetailRequest.getLatitude());
        addressDetail.setLongitude(addressDetailRequest.getLongitude());
        addressDetail.setPlaceId(addressDetailRequest.getPlaceId());
        addressDetail.setPlaceType(addressDetailRequest.getPlaceType());
        addressDetail.setSecondaryText(addressDetailRequest.getSecondaryText());
        addressDetail.setCountry(addressDetailRequest.getCountry());
        addressDetail.setPincode(addressDetailRequest.getPincode());
        logger.debug("Created AddressDetail entity for placeId: {}", addressDetailRequest.getPlaceId());
        return addressDetail;
    }
}

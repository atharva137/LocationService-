package com.ray.LocationService.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ray.LocationService.bean.LocationResponse;
import com.ray.LocationService.config.CaffeineCache;
import com.ray.LocationService.exception.LocationServiceException;
import com.ray.LocationService.service.OlaService;
import com.ray.LocationService.utill.RestTemplateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


@Service
public class OlaServiceImpl implements OlaService {

    private static final String OLA_API_URL_TEMPLATE = "https://api.olamaps.io/places/v1/autocomplete?input=%s&api_key=fpOlWsZTrkx49lItg5U4W1zX07SjjvC7dbPYlQx8";
    private static final Logger logger = LoggerFactory.getLogger(OlaServiceImpl.class);

    private final RestTemplateUtil restTemplateUtil;
    private final ObjectMapper objectMapper;
    private final CaffeineCache<String, List<LocationResponse>> locationCache;

    @Autowired
    public OlaServiceImpl(RestTemplateUtil restTemplateUtil, ObjectMapper objectMapper,
                          CaffeineCache<String, List<LocationResponse>> locationCache) {
        this.restTemplateUtil = restTemplateUtil;
        this.objectMapper = objectMapper;
        this.locationCache = locationCache;
    }

    @Async("taskExecutor")  // asynchronous method
    @Override
    public CompletableFuture<List<LocationResponse>> searchLocations(String query) {
        try {
            // Rate-limiting check

            // Check cache first
            List<LocationResponse> cachedResponse = locationCache.get(query);
            if (cachedResponse != null) {
                logger.info("Cache hit for query: {}", query);
                return CompletableFuture.completedFuture(cachedResponse);
            }

            // API request if no cache hit
            String apiUrl = String.format(OLA_API_URL_TEMPLATE, query);
            String locationJsonRes = restTemplateUtil.get(apiUrl, null, null, String.class);

            List<LocationResponse> locationResponseList = processApiResponse(locationJsonRes);

            // Cache the result
            locationCache.put(query, locationResponseList);

            return CompletableFuture.completedFuture(locationResponseList);
        } catch (Exception ex) {
            logger.error("Error while fetching location data for query: {}", query, ex);
            throw new LocationServiceException("Failed to fetch or process location data", ex);
        }
    }

    /**
     * Processes the OLA API response and converts it into a list of LocationResponse objects.
     * @param locationJsonRes The JSON response from the OLA API.
     * @return A list of LocationResponse objects.
     */
    private List<LocationResponse> processApiResponse(String locationJsonRes) {
        List<LocationResponse> locationResponseList = new ArrayList<>();
        logger.debug("Start ProcessAPI Response " + locationJsonRes);
        try {
            JsonNode locationRootJson = objectMapper.readTree(locationJsonRes);

            if ("ok".equals(locationRootJson.path("status").asText())) {
                JsonNode predictions = locationRootJson.path("predictions");

                locationResponseList = StreamSupport.stream(predictions.spliterator(), true).parallel().map((prediction) -> {
                    LocationResponse locationResponse = new LocationResponse();
                    locationResponse.setPlaceId(Optional.ofNullable(prediction.path("place_id").asText()).orElse("")); // or any default value
                    locationResponse.setDescription(Optional.ofNullable(prediction.path("description").asText()).orElse("")); // or any default value
                    JsonNode structuredFormatting = prediction.path("structured_formatting");
                    locationResponse.setMainText(Optional.ofNullable(structuredFormatting.path("main_text").asText()).orElse("")); // or any default value
                    locationResponse.setSecondaryText(Optional.ofNullable(structuredFormatting.path("secondary_text").asText()).orElse("")); // or any default value
                    JsonNode geometry = prediction.path("geometry");
                    JsonNode location = geometry.path("location");
                    locationResponse.setLatitude(Optional.of(location.path("lat").asDouble()).orElse(0.0)); // or any default value
                    locationResponse.setLongitude(Optional.of(location.path("lng").asDouble()).orElse(0.0)); // or any default value
                    locationResponse.setPlaceType(Optional.ofNullable(prediction.path("types").get(0)).map(JsonNode::asText).orElse("")); // or any default value

                    return locationResponse;

                }).collect(Collectors.toList());

            }

            logger.debug("END ProcessAPI Response " + locationJsonRes);
        } catch (Exception ex) {
            logger.error("Error while processing OLA API response", ex);
            throw new LocationServiceException("Failed to process location data from the OLA API", ex);
        }
        return locationResponseList;
    }

}

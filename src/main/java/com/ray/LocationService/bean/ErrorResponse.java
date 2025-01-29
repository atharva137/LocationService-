package com.ray.LocationService.bean;

public class ErrorResponse {

    private String message;
    private int value;

    public ErrorResponse(String message, int value) {

        this.message = message;
        this.value  = value;
    }
}

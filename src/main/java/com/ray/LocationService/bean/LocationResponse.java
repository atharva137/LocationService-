package com.ray.LocationService.bean;



import java.util.Objects;

public class LocationResponse {

    private String placeId;       // Unique identifier for the location
    private String mainText;          // Name of the location
    private String secondaryText; // Formatted address of the location

    private String description;
    private Double latitude;      // Latitude of the location
    private Double longitude;     // Longitude of the location
    private String placeType;     // Type of place (e.g., restaurant, park, etc.)

    public LocationResponse() {}

    public LocationResponse(String placeId, String mainText, String secondaryText, String description, Double latitude, Double longitude, String placeType) {
        this.placeId = placeId;
        this.mainText = mainText;
        this.secondaryText = secondaryText;
        this.description = description;
        this.latitude = latitude;
        this.longitude = longitude;
        this.placeType = placeType;
    }

    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public String getMainText() {
        return mainText;
    }

    public void setMainText(String mainText) {
        this.mainText = mainText;
    }

    public String getSecondaryText() {
        return secondaryText;
    }

    public void setSecondaryText(String secondaryText) {
        this.secondaryText = secondaryText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPlaceType() {
        return placeType;
    }

    public void setPlaceType(String placeType) {
        this.placeType = placeType;
    }


    @Override
    public String toString() {
        return "LocationResponse{" +
                "placeId='" + placeId + '\'' +
                ", mainText='" + mainText + '\'' +
                ", secondaryText='" + secondaryText + '\'' +
                ", description='" + description + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", placeType='" + placeType + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LocationResponse that)) return false;
        return getPlaceId().equals(that.getPlaceId()) && getMainText().equals(that.getMainText()) && getSecondaryText().equals(that.getSecondaryText()) && getDescription().equals(that.getDescription()) && getLatitude().equals(that.getLatitude()) && getLongitude().equals(that.getLongitude()) && getPlaceType().equals(that.getPlaceType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlaceId(), getMainText(), getSecondaryText(), getDescription(), getLatitude(), getLongitude(), getPlaceType());
    }

}


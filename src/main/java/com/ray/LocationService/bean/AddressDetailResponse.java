package com.ray.LocationService.bean;

public class AddressDetailResponse {


    private Long addressId;
    private String placeId;          // Unique identifier from the external API (e.g., "ola-platform:23b003549ef19e539bf198a45eb51ed1")
    private String mainText;         // Main address text, typically city/region (e.g., "Varanasi, Uttar Pradesh, 221001, India")
    private String secondaryText;    // Secondary address details
    private String description;      // Detailed description of the location (e.g., "Kodai Chowki, Varanasi, Uttar Pradesh, 221001, India")
    private Double latitude;         // Latitude of the location
    private Double longitude;        // Longitude of the location
    private String placeType;        // Type of place (e.g., "neighborhood")
    private String pincode;          // Postal code (e.g., "221001")
    private String country;    // Country name (e.g., "India")


    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }
}

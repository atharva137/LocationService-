package com.ray.LocationService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;


@Entity
@Table(name = "address", schema = "alpha")
public class AddressDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long addressId;

    @NotNull
    @Column(name = "place_id", unique = true)
    private String placeId; // Unique ID from the external API

    @NotNull
    @Column(name = "main_text")
    private String mainText; // Main address text (e.g., city, region)

    @Column(name = "secondary_text")
    private String secondaryText; // Secondary address details

    @Column(name = "description")
    private String description; // Detailed description of the location

    @NotNull
    @Column(name = "latitude")
    private Double latitude; // Latitude for geolocation

    @NotNull
    @Column(name = "longitude")
    private Double longitude; // Longitude for geolocation

    @Column(name = "place_type")
    private String placeType; // Type of place (e.g., neighborhood, landmark)

    @Column(name = "pincode")
    private String pincode; // Postal code for the address

    @Column(name = "country")
    private String country; // Country for the address

    // Getters and Setters
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

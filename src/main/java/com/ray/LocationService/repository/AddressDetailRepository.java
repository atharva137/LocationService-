package com.ray.LocationService.repository;

import com.ray.LocationService.entity.AddressDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AddressDetailRepository extends JpaRepository<AddressDetail, Long> {

    public AddressDetail findByPlaceId(String placeId);
}

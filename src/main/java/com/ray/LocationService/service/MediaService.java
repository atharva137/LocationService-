package com.ray.LocationService.service;

import com.ray.LocationService.bean.MediaResponse;

import java.util.List;

public interface MediaService  {

    public List<MediaResponse> searchMedia(String query);
}

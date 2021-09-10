package com.examensupletorio.visorlugares.service;

import com.examensupletorio.visorlugares.model.NearbyPlacesResult;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IGoogleMapsService {
    @GET("json")
    public Call<NearbyPlacesResult> getPlaces(@Query(value = "type") String type, @Query("radius") int radius, @Query(value = "location", encoded = false) String location, @Query("key") String key);
}

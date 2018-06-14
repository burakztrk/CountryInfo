package com.ozturkburak.mapconquer.net;



import com.ozturkburak.mapconquer.model.GeoJsonInfo;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GeojsonService
{

    @GET("burakztrk/world.geo.json/master/countries/{countryId}.geo.json")
    Call<GeoJsonInfo> getCountryLayer(@Path("countryId") String countryId);
//
//    @GET("group/{id}/users")
//    Call<List<User>> groupList(@Path("id") int groupId, @Query("sort") String sort);
//
//    @POST("users/new")
//    Call<User> createUser(@Body User user);
}
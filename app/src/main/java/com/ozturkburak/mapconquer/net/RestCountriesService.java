package com.ozturkburak.mapconquer.net;



import com.ozturkburak.mapconquer.model.restcountries.RestCountriesResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestCountriesService
{
    @GET("rest/v2/alpha/{countryId}")
    Call<RestCountriesResponse> getCountryInfo(@Path("countryId") String countryId);

}
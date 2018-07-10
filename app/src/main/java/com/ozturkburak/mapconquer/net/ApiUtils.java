package com.ozturkburak.mapconquer.net;

public class ApiUtils {
    public static final String BASE_URL = "https://restcountries.eu/";

    public static RestCountriesService restCountriesService() {
        return RetrofitBuilder.getClient(BASE_URL).create(RestCountriesService.class);
    }
}

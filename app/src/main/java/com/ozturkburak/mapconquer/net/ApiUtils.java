package com.ozturkburak.mapconquer.net;

public class ApiUtils {
    public static final String BASE_URL = "http://raw.githubusercontent.com/";

    public static GeojsonService getGeoJsonService() {
        return RetrofitBuilder.getClient(BASE_URL).create(GeojsonService.class);
    }
}

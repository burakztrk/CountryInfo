package com.ozturkburak.mapconquer;


import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;



import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.gson.Gson;

import com.ozturkburak.mapconquer.model.CountryInfo;
import com.ozturkburak.mapconquer.model.newmodel.GeoJsonInfo;
import com.ozturkburak.mapconquer.net.ApiUtils;
import com.ozturkburak.mapconquer.net.GeojsonService;
import com.ozturkburak.mapconquer.utils.CountryParser;
import com.ozturkburak.mapconquer.utils.GoogleMapsUtils;
import com.ozturkburak.mapconquer.utils.StreamUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback , View.OnClickListener {

    private GoogleMap mMap;
    private GeojsonService mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mService = ApiUtils.getGeoJsonService();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    private void loadCountries()
    {
//        List<String> countryCodeList = new ArrayList<>();
//        countryCodeList.add("TUR");
//        countryCodeList.add("USA");
//        countryCodeList.add("AFG");
//
//        Callback<GeoJsonInfo> callback = new Callback<GeoJsonInfo>() {
//            @Override
//            public void onResponse(Call<GeoJsonInfo> call, Response<GeoJsonInfo> response)
//            {
//                CountryInfo country = null;
//                if (response.body() !=null)
//                    country = CountryParser.Parse(response.body());
//
//                if (country ==null) return;
//
//                GoogleMapsUtils.AddCountryOnMap(getApplicationContext() ,mMap , country );
//            }
//
//            @Override
//            public void onFailure(Call<GeoJsonInfo> call, Throwable t)
//            {
//                Toast.makeText(MapsActivity.this ,t.getLocalizedMessage() , Toast.LENGTH_LONG).show();
//            }
//        };
//
//        for (String code : countryCodeList)
//        {
//            mService.getCountryLayer(code).enqueue(callback);
//        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
//        loadCountries();
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.mapactivityButtonOk:
                String json = StreamUtils.InputStreamToString(this.getResources().openRawResource(R.raw.countriesgeo));
                GeoJsonInfo geoJsonInfo = new Gson().fromJson(json , com.ozturkburak.mapconquer.model.newmodel.GeoJsonInfo.class);

                //todo newmodel isimlendirme degisecek
                //todo farkli threade al

                if (geoJsonInfo != null && geoJsonInfo.getFeatures() !=null)
                {
                    CountryParser countryParser = new CountryParser(geoJsonInfo);
                    List<CountryInfo> countries = countryParser.getCountries();

                    for (CountryInfo country : countries)
                    {
                        GoogleMapsUtils.AddCountryOnMap(getApplicationContext() , mMap ,country);
                    }
                }
                break;
                default:
                    break;
        }
    }
}

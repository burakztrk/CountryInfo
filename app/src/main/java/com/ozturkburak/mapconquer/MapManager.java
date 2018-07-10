package com.ozturkburak.mapconquer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.PictureDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.bumptech.glide.RequestBuilder;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.gson.Gson;
import com.ozturkburak.mapconquer.model.BindCountryData;
import com.ozturkburak.mapconquer.model.geojson.CountryInfo;
import com.ozturkburak.mapconquer.model.geojson.GeoJsonInfo;
import com.ozturkburak.mapconquer.model.restcountries.RestCountriesResponse;
import com.ozturkburak.mapconquer.net.ApiUtils;
import com.ozturkburak.mapconquer.utils.CommonVariables;
import com.ozturkburak.mapconquer.utils.CountryParser;
import com.ozturkburak.mapconquer.utils.GoogleMapsUtils;
import com.ozturkburak.mapconquer.utils.StreamUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



class MapManager extends AsyncTask<Void , Void , List<CountryInfo>>
        implements GoogleMap.OnPolygonClickListener,
        GoogleMap.OnCameraIdleListener
{
    private Context context;
    private GoogleMap googleMap;
    private BindCountryData bindCountryData;

    private List<CountryInfo> countries;
    private List<CountryInfo> showedCountries;
    private CountryInfo selectedCountry;
    private CountryInfo beforeSelectedCountry;


    public MapManager(Context context, GoogleMap map, BindCountryData bindCountryData) {
        this.context = context;
        this.googleMap = map;
        this.bindCountryData = bindCountryData;

        showedCountries = new ArrayList<>();
        this.googleMap.setMaxZoomPreference(5f);
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnPolygonClickListener(this);


    }



    public void drawCountryArea() {
        ((MapsActivity)context).showProgressDialog();
        this.execute();
    }



    @Override
    public void onCameraIdle()
    {
        refreshMapView();
    }


    public void refreshMapView()
    {
        if (this.countries ==null)
            return;

        showedCountries.clear();

        //boundsa padding ekleme
        LatLngBounds bounds = googleMap.getProjection().getVisibleRegion().latLngBounds;
        for (CountryInfo country :countries)
        {
            if (bounds.contains(country.getBounds().getCenter()))
                showedCountries.add(country);
        }

        googleMap.clear();
        if (selectedCountry != null) {
            GoogleMapsUtils.AddCountryOnMap(context , googleMap , selectedCountry);
        }

        GoogleMapsUtils.AddCountriesOnMap(context , googleMap , showedCountries);


    }




    @Override
    public void onPolygonClick(Polygon polygon) {


        this.selectedCountry = (CountryInfo) polygon.getTag();
        this.beforeSelectedCountry = selectedCountry;

        //secilen ulkenin isaretlenmesi
        for (Polygon selectedPolygon : selectedCountry.getPolygons())
        {
            selectedPolygon.setFillColor(ContextCompat.getColor(context , R.color.selectedCountryColor));
            selectedPolygon.setGeodesic(true);
            selectedPolygon.setStrokeWidth(10);
            selectedPolygon.setStrokeColor(selectedCountry.getColor());
            selectedPolygon.setZIndex(100);
        }

        //secilen ulkeyi ortala
        CameraPosition newCamPos = new CameraPosition(selectedCountry.getBounds().getCenter(), 4f, googleMap.getCameraPosition().tilt, googleMap.getCameraPosition().bearing);
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(newCamPos), 1000, null);

        showInfoView(selectedCountry);

    }


    private void showInfoView(final CountryInfo selectedCountry) {

        if(!((MapsActivity)context).isInfoViewFullShowed())
            ((MapsActivity)context).minimizeInfoView();



        Call<RestCountriesResponse> restCountriesService = ApiUtils.restCountriesService().getCountryInfo(selectedCountry.getId());
        restCountriesService.enqueue(new Callback<RestCountriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestCountriesResponse> call, @NonNull Response<RestCountriesResponse> response) {
                if (response.body() != null)
                {
                    RestCountriesResponse restCountriesResponse = response.body();

                    String speeling="";
                    if (restCountriesResponse.getAltSpellings() !=null && !restCountriesResponse.getAltSpellings().isEmpty())
                    {
                        speeling = restCountriesResponse.getAltSpellings().get(0);
                        for (String spell : restCountriesResponse.getAltSpellings())
                            if (spell.length() > speeling.length())
                                speeling = spell;
                    }

                    String currencies = "";
                    if (restCountriesResponse.getCurrencies() !=null && !restCountriesResponse.getCurrencies().isEmpty())
                        currencies =String.format("%s/%s" ,restCountriesResponse.getCurrencies().get(0).getName() , restCountriesResponse.getCurrencies().get(0).getCode());


                    bindCountryData.setFlagUrl(restCountriesResponse.getFlag());
                    bindCountryData.setCountryName(String.format("%s (%s)", restCountriesResponse.getName() , restCountriesResponse.getNativeName()));
                    bindCountryData.setCapitalName(restCountriesResponse.getCapital());
                    bindCountryData.setSpelling(speeling);
                    bindCountryData.setRegion(restCountriesResponse.getRegion());
                    bindCountryData.setPopulation(String.format(Locale.getDefault() ,"%.2fM", restCountriesResponse.getPopulation()/ 1000000.0));
                    bindCountryData.setArea(String.format(Locale.getDefault() ,"%.2fM Km2", restCountriesResponse.getArea()/ 100000.0));
                    bindCountryData.setCurrency(currencies);
                    bindCountryData.setBorders(restCountriesResponse.getBorders());

                    ((MapsActivity)context).borderAdapterDataChanged(bindCountryData.getBorders());

                    ((MapsActivity)context).hideInfoView();

                }
            }

            @Override
            public void onFailure(@NonNull Call<RestCountriesResponse> call, @NonNull Throwable t) { }
        });


        if (beforeSelectedCountry != null) {
            //secilen ulkenin geri  renklendirilmesi
            for (Polygon beforeSelectedPolygon : beforeSelectedCountry.getPolygons()) {
//                beforeSelectedPolygon.setFillColor(beforeSelectedCountry.getColor());
                beforeSelectedPolygon.setFillColor(Color.argb(0,255,0,0));
                beforeSelectedPolygon.setGeodesic(false);
                beforeSelectedPolygon.setStrokeWidth(1);
                beforeSelectedPolygon.setZIndex(1);
            }
        }
    }














    //parse countryborder
    @Override
    protected List<CountryInfo> doInBackground(Void... voids) {
        List<CountryInfo> countries = new ArrayList<>();

        String json = StreamUtils.InputStreamToString(context.getResources().openRawResource(R.raw.countriesgeo));
        GeoJsonInfo geoJsonInfo = new Gson().fromJson(json, GeoJsonInfo.class);

        if (geoJsonInfo != null && geoJsonInfo.getFeatures() != null) {
            CountryParser countryParser = new CountryParser(geoJsonInfo);
            countries = countryParser.getCountries();
        }

        return countries;
    }

    @Override
    protected void onPostExecute(final List<CountryInfo> countryInfos) {
        super.onPostExecute(countryInfos);

        this.countries = countryInfos;
        ((MapsActivity)context).hideProgressDialog();
        refreshMapView();
    }

}

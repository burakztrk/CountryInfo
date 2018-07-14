package com.ozturkburak.mapconquer;

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;

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
import com.ozturkburak.mapconquer.utils.CountryParser;
import com.ozturkburak.mapconquer.utils.GoogleMapsUtils;
import com.ozturkburak.mapconquer.utils.StreamUtils;

import java.lang.ref.WeakReference;
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
    private WeakReference<MapsActivity> activityWeakReference;
    private GoogleMap googleMap;


    private List<CountryInfo> countries;
    private List<CountryInfo> showedCountries;
    private CountryInfo selectedCountry;
    private CountryInfo beforeSelectedCountry;


    MapManager(MapsActivity context, GoogleMap map)
    {
        activityWeakReference = new WeakReference<>(context);

        this.googleMap = map;

        this.showedCountries = new ArrayList<>();
        this.googleMap.setMaxZoomPreference(5f);
        this.googleMap.setOnCameraIdleListener(this);
        this.googleMap.setOnPolygonClickListener(this);


    }


    public void drawCountryArea() {
        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return;

        activity.showProgressDialog();
        this.execute();
    }



    @Override
    public void onCameraIdle() { refreshMapView(); }


    private void refreshMapView()
    {
        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return;


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
            GoogleMapsUtils.AddCountryOnMap(activity , googleMap , selectedCountry);
        }

        GoogleMapsUtils.AddCountriesOnMap(activity , googleMap , showedCountries);

    }




    @Override
    public void onPolygonClick(Polygon polygon) {

        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return;

        this.selectedCountry = (CountryInfo) polygon.getTag();
        this.beforeSelectedCountry = selectedCountry;

        //secilen ulkenin isaretlenmesi
        for (Polygon selectedPolygon : selectedCountry.getPolygons())
        {
            selectedPolygon.setFillColor(ContextCompat.getColor(activity, R.color.selectedCountryColor));
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


    private void showInfoView(final CountryInfo selectedCountry)
    {
        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return;

        if(!activity.isInfoViewFullShowed())
            activity.minimizeInfoView();



        Call<RestCountriesResponse> restCountriesService = ApiUtils.restCountriesService().getCountryInfo(selectedCountry.getId());
        restCountriesService.enqueue(new Callback<RestCountriesResponse>() {
            @Override
            public void onResponse(@NonNull Call<RestCountriesResponse> call, @NonNull Response<RestCountriesResponse> response) {
                MapsActivity activity = activityWeakReference.get();
                if (activity == null || activity.isFinishing())
                    return;

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

                    BindCountryData newBindCountryData = new BindCountryData();
                    newBindCountryData.setFlagUrl(restCountriesResponse.getFlag());
                    newBindCountryData.setCountryName(String.format("%s (%s)", restCountriesResponse.getName() , restCountriesResponse.getNativeName()));
                    newBindCountryData.setCapitalName(restCountriesResponse.getCapital());
                    newBindCountryData.setSpelling(speeling);
                    newBindCountryData.setRegion(restCountriesResponse.getRegion());
                    newBindCountryData.setPopulation(String.format(Locale.getDefault() ,"%.2fM", restCountriesResponse.getPopulation()/ 1000000.0));
                    newBindCountryData.setArea(String.format(Locale.getDefault() ,"%.2fM Km2", restCountriesResponse.getArea()/ 100000.0));
                    newBindCountryData.setCurrency(currencies);
                    newBindCountryData.setBorders(restCountriesResponse.getBorders());

                    activity.updateBindingData(newBindCountryData);
//                    activity.borderAdapterDataChanged(newBindCountryData.getBorders());

                    activity.hideInfoView();

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
        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return null;


        List<CountryInfo> countries = new ArrayList<>();

        String json = StreamUtils.InputStreamToString(activity.getResources().openRawResource(R.raw.countriesgeo));
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

        MapsActivity activity = activityWeakReference.get();
        if (activity == null || activity.isFinishing())
            return;

        this.countries = countryInfos;
        activity.hideProgressDialog();
        refreshMapView();
    }

}

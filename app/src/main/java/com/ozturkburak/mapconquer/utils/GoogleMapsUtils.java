package com.ozturkburak.mapconquer.utils;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.graphics.ColorUtils;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.android.gms.maps.model.TileProvider;
import com.ozturkburak.mapconquer.MapsActivity;
import com.ozturkburak.mapconquer.model.CountryInfo;

import java.util.List;
import java.util.Random;

public class GoogleMapsUtils
{

    private static LatLngBounds getPolygonBounds(List<LatLng> polygonPointsList)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(LatLng latLng : polygonPointsList)
            builder.include(latLng);

        return builder.build();
    }

    public static void AddCountryOnMap(final Context context, GoogleMap mMap, CountryInfo country)
    {
        for (int i = 0 ; i <country.getArea().size() ; i++)
        {
            PolygonOptions polygonOptions = country.getArea().get(i);
            Polygon polygon = mMap.addPolygon(polygonOptions);
            polygon.setTag(country);
            polygon.setClickable(true);
            polygon.setStrokeWidth(0.1f);
            polygon.setFillColor(Utils.getMatColor(context , "100"));
            polygon.setGeodesic(true);
            polygon.setZIndex(3);

            if (country.getBigArea() == null)
                country.setBigArea(polygon);
            else
                {
                if (polygon.getPoints().size() > country.getBigArea().getPoints().size())
                    country.setBigArea(polygon);
            }
        }


//            LatLngBounds bounds =getPolygonBounds(country.getBigArea().getPoints());
//            MarkerOptions markerOptions = new MarkerOptions();
//            markerOptions.position(bounds.getCenter()).title(country.getName());
//
//            mMap.addMarker(markerOptions);
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(bounds.getCenter()));

            mMap.setOnPolygonClickListener(new GoogleMap.OnPolygonClickListener()
            {
                @Override
                public void onPolygonClick(Polygon polygon) {
                    CountryInfo countryInfo = (CountryInfo) polygon.getTag();
                    Toast.makeText(context , countryInfo.getName(), Toast.LENGTH_SHORT).show();
                }
            });

    }


}

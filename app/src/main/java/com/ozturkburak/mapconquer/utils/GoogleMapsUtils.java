package com.ozturkburak.mapconquer.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.ozturkburak.mapconquer.model.geojson.CountryInfo;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.List;

public class GoogleMapsUtils
{

    public static LatLngBounds CalculatePolygonBounds(List<PolygonOptions> polygonOptionsList)
    {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for(PolygonOptions polygonOptions : polygonOptionsList)
        {
            for (LatLng latLng : polygonOptions.getPoints())
                builder.include(latLng);
        }

        return builder.build();
    }


    public static void AddCountriesOnMap(final Context context, final GoogleMap mMap,final List<CountryInfo> countries)
    {
        for (CountryInfo country:countries)
        {
            for (int i = 0; i <country.getPolygonOptions().size() ; i++)
            {
                if (country.getColor() == 0)
                    country.setColor(Utils.getMatColor(context,"300"));

                PolygonOptions polygonOptions = country.getPolygonOptions().get(i);
                Polygon polygon = mMap.addPolygon(polygonOptions);
                polygon.setTag(country);
                polygon.setClickable(true);
                polygon.setStrokeWidth(0.1f);
//                polygon.setFillColor(country.getColor());
                country.getPolygons().add(polygon);
            }
        }
    }

    public static void AddCountryOnMap(final Context context, final GoogleMap mMap,final CountryInfo country)
    {

        for (int i = 0; i <country.getPolygonOptions().size() ; i++)
        {
            if (country.getColor() == 0)
             country.setColor(Utils.getMatColor(context,"300"));

            PolygonOptions polygonOptions = country.getPolygonOptions().get(i);
            Polygon polygon = mMap.addPolygon(polygonOptions);
            polygon.setTag(country);
            polygon.setClickable(true);
            polygon.setStrokeWidth(0.1f);
            polygon.setFillColor(country.getColor());
            country.getPolygons().add(polygon);


        }
    }






    private static double calculateCountryArea(CountryInfo country)
    {
        if (country.getCalculatedArea() == 0)
        {
            double result = 0d;
            for (Polygon polygon :country.getPolygons())
                result += SphericalUtil.computeArea(polygon.getPoints());

            country.setCalculatedArea(result);
        }

        return country.getCalculatedArea();
    }

    public static CountryInfo GetMax(CountryInfo country1, CountryInfo country2)
    {
        if (calculateCountryArea(country1) > calculateCountryArea(country2))
            return  country1;
        else
            return country2;
    }


    public static double  getCoefficient(CountryInfo country1 , CountryInfo country2)
    {
        BigDecimal country1Area = BigDecimal.valueOf(calculateCountryArea(country1));
        BigDecimal country2Area = BigDecimal.valueOf(calculateCountryArea(country2));

        BigDecimal min = country1Area.min(country2Area);
        BigDecimal max = country1Area.max(country2Area);
        if (max == country1Area)
            return 1f;

        return max.divide(min,MathContext.DECIMAL32).doubleValue();
    }


}

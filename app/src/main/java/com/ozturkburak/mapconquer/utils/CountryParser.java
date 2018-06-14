package com.ozturkburak.mapconquer.utils;





import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolygonOptions;
import com.ozturkburak.mapconquer.model.CountryInfo;
import com.ozturkburak.mapconquer.model.newmodel.Feature;

import java.util.ArrayList;
import java.util.List;

public class CountryParser
{
    com.ozturkburak.mapconquer.model.newmodel.GeoJsonInfo mGeoJsonInfo;
    public CountryParser(com.ozturkburak.mapconquer.model.newmodel.GeoJsonInfo geoJsonInfo)
    {
        mGeoJsonInfo = geoJsonInfo;
    }

    public List<CountryInfo> getCountries()
    {
        List<CountryInfo> countries = new ArrayList<>();
        if (mGeoJsonInfo.getFeatures() != null)
        {
            for(Feature feature : mGeoJsonInfo.getFeatures())
            {
                CountryInfo countryInfo = new CountryInfo();
                countryInfo.setId(feature.getId());
                countryInfo.setName(feature.getProperties().getName());

                if (feature.getGeometry() !=null && feature.getGeometry().getCoordinates() !=null)
                {
                    List<List<List<Object>>> coordinatesList = (List<List<List<Object>>>) feature.getGeometry().getCoordinates();
                    for (List<List<Object>> listlistlist : coordinatesList)//2
                    {
                        PolygonOptions polygonOptions = new PolygonOptions();
                        polygonOptions.clickable(true);
                        countryInfo.getArea().add(polygonOptions);
                        if (listlistlist.get(0) != null && !listlistlist.get(0).isEmpty())
                        {

                            try{
                                List<Object> coordinates = (List<Object>) listlistlist.get(0);
                                for (Object  coordinateObj : coordinates)
                                {
                                    List<Double> coordinate = (List<Double>) coordinateObj;
                                    polygonOptions.add(new LatLng(coordinate.get(1) , coordinate.get(0)));
                                }
                            }catch (Throwable t)
                            {
                                for(List<Object> coordinates :listlistlist)
                                {
                                    Double lng = (Double)coordinates.get(0);
                                    Double lat = (Double)coordinates.get(1);
                                    polygonOptions.add(new LatLng(lat ,lng));
                                }
                            }
                        }
                    }
                }
                countries.add(countryInfo);
            }
        }




        return countries;
    }



}

package com.ozturkburak.mapconquer.model.geojson;

import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.ozturkburak.mapconquer.utils.GoogleMapsUtils;

import java.util.ArrayList;
import java.util.List;

public class CountryInfo
{
    private String id = "";
    private String  name = "";
    private List<PolygonOptions> polygonOptions ;
    private List<Polygon> polygons;
    private Polygon bigArea ;
    private LatLngBounds bounds;
    private int color;
    private double calculatedArea;


    public CountryInfo()
    {
        polygonOptions = new ArrayList<>();
        polygons = new ArrayList<>();
    }

    public LatLngBounds getBounds()
    {
        if (bounds == null)
            bounds = GoogleMapsUtils.CalculatePolygonBounds(this.polygonOptions);
        return bounds;
    }

    public void setBounds(LatLngBounds bounds) {
        this.bounds = bounds;
    }

    public double getCalculatedArea() { return calculatedArea; }

    public void setCalculatedArea(double calculatedArea) { this.calculatedArea = calculatedArea; }

    public List<Polygon> getPolygons() { return polygons; }

    public void setPolygons(List<Polygon> polygons) { this.polygons = polygons; }

    public int getColor() { return color; }

    public void setColor(int color) { this.color = color; }

    public Polygon getBigArea() {
        if (bigArea == null)
        {
            for (Polygon polygon : this.polygons)
            {
                if (bigArea == null)
                    bigArea = polygon;

                if (bigArea.getPoints().size() < polygon.getPoints().size())
                    bigArea = polygon;
            }
        }

        return bigArea;
    }

    public void setBigArea(Polygon bigArea) {
        this.bigArea = bigArea;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<PolygonOptions> getPolygonOptions() {
        return polygonOptions;
    }

    public void setPolygonOptions(List<PolygonOptions> polygonOptions) {
        this.polygonOptions = polygonOptions;
    }
}

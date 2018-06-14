package com.ozturkburak.mapconquer.model;

import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class CountryInfo
{
    private String id = "";
    private String  name = "";
    private List<PolygonOptions> area  = new ArrayList<>();
    private Polygon bigArea ;


    public Polygon getBigArea() {
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

    public List<PolygonOptions> getArea() {
        return area;
    }

    public void setArea(List<PolygonOptions> area) {
        this.area = area;
    }
}

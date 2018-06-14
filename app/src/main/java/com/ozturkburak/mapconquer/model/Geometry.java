
package com.ozturkburak.mapconquer.model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Geometry
{
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("coordinates")
    @Expose
//    private List<List<List<List<Double>>>> coordinates = null;
    private Object coordinates = null;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Geometry withType(String type) {
        this.type = type;
        return this;
    }

    public Object getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Object coordinates) {
        this.coordinates = coordinates;
    }

    public Geometry withCoordinates(Object coordinates) {
        this.coordinates = coordinates;
        return this;
    }

}

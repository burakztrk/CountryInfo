package com.ozturkburak.mapconquer.model;


import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.graphics.drawable.PictureDrawable;
import android.widget.ImageView;

import com.android.databinding.library.baseAdapters.BR;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ozturkburak.mapconquer.utils.svg.SvgSoftwareLayerSetter;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BindCountryData extends BaseObservable {
    private String flagUrl;
    private String countryName;
    private String capitalName;
    private String spelling;
    private String region;
    private String population;
    private String area;
    private String currency;
    private List<String> borders;

    public BindCountryData()
    {
        this("");
    }

    public BindCountryData(String countryName)
    {
        this.countryName = countryName;
        flagUrl = "";
        capitalName = "";
        spelling = "";
        region = "";
        population = "";
        area = "";
        currency = "";
        borders = new ArrayList<>();
    }

    @Bindable
    public String getFlagUrl() {
        return flagUrl;
    }

    @Bindable
    public String getCountryName() {
        return countryName;
    }

    @Bindable
    public String getCapitalName() {
        return capitalName;
    }
    @Bindable
    public String getSpelling() {
        return spelling;
    }

    @Bindable
    public String getRegion() {
        return region;
    }
    @Bindable
    public String getPopulation() {
        return population;
    }
    @Bindable
    public String getArea() {
        return area;
    }
    @Bindable
    public String getCurrency() {
        return currency;
    }
    @Bindable
    public List<String> getBorders() { return borders; }


    public BindCountryData setFlagUrl(String flagUrl)
    {
        this.flagUrl = flagUrl;
        notifyPropertyChanged(BR.flagUrl);
        return this;
    }

    @BindingAdapter("flagUrl")
    public static void loadImage(ImageView imageView, String url)
    {
        Context context = imageView.getContext();
        RequestBuilder requestBuilder = Glide.with(context)
                .as(PictureDrawable.class)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
        requestBuilder.load(url).into(imageView);
    }



    public BindCountryData setCountryName(String countryName) {
        this.countryName = countryName;
        notifyPropertyChanged(BR.countryName);
        return this;
    }



    public BindCountryData setCapitalName(String capitalName) {
        this.capitalName = capitalName;
        notifyPropertyChanged(BR.capitalName);
        return this;
    }



    public BindCountryData setSpelling(String spelling) {
        this.spelling = spelling;
        notifyPropertyChanged(BR.spelling);
        return this;
    }



    public BindCountryData setRegion(String region) {
        this.region = region;
        notifyPropertyChanged(BR.region);
        return this;
    }


    public BindCountryData setPopulation(String population) {
        this.population = population;
        notifyPropertyChanged(BR.population);
        return this;
    }


    public BindCountryData setArea(String area) {
        this.area = area;
        notifyPropertyChanged(BR.area);
        return this;
    }


    public BindCountryData setCurrency(String currency) {
        this.currency = currency;
        notifyPropertyChanged(BR.currency);
        return this;
    }


    public BindCountryData setBorders(List<String> borders) {
        this.borders = borders;
        notifyPropertyChanged(BR.borders);
        return this;
    }
}

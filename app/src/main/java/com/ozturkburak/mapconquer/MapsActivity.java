package com.ozturkburak.mapconquer;


import android.databinding.DataBindingUtil;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;


import com.ozturkburak.mapconquer.databinding.ActivityMapsBinding;
import com.ozturkburak.mapconquer.model.BindCountryData;
import com.ozturkburak.mapconquer.utils.CommonVariables;
import com.ozturkburak.mapconquer.utils.OnSwipeTouchListener;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements  OnMapReadyCallback
{
    private View infoView;
    private ProgressBar progressBar ;
    private MapManager mapManager;
    private BindCountryData countryData ;
    private boolean infoViewFullShowed;

    private RecyclerView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_maps);

        ActivityMapsBinding binding = DataBindingUtil.setContentView(this , R.layout.activity_maps);
        countryData = new BindCountryData();
        binding.setCountryInfo(countryData);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        progressBar = findViewById(R.id.maps_progressBar);
        infoView = findViewById(R.id.cardview_include_info);

        //border listview
        listView = infoView.findViewById(R.id.cardview_layout_borderview);
        listView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        listView.setAdapter(new BorderAdapter(this, new ArrayList<String>()));
        listView.getAdapter().notifyDataSetChanged();
        ((BorderAdapter)listView.getAdapter()).setClickListener(null);



        infoView.setTranslationY(CommonVariables.VIEW_FIRSTSTATE);
        infoView.setOnTouchListener(new OnSwipeTouchListener(this)
        {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return super.onTouch(view, motionEvent);
            }

            @Override
            public void onSwipeUp()
            {
                fullShowInfoView();
                super.onSwipeUp();
            }

            @Override
            public void onSwipeDown()
            {
                hideInfoView();
                super.onSwipeDown();
            }
        });


    }



    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mapManager = new MapManager(this , googleMap , countryData);
        mapManager.drawCountryArea();
    }

    public void showProgressDialog()
    {
        if (progressBar!=null)
            progressBar.setVisibility(View.VISIBLE);
    }


    public void hideProgressDialog()
    {
        if (progressBar !=null)
        progressBar.setVisibility(View.GONE);
    }


    public void borderAdapterDataChanged(List<String> borders)
    {
        BorderAdapter adapter = new BorderAdapter(this, borders);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }



    public void fullShowInfoView()
    {
        infoViewFullShowed = true;
        infoView.animate()
                .translationY(0)
                .setDuration(CommonVariables.ANIMATION_DURATION);
    }



    public void minimizeInfoView()
    {
        infoViewFullShowed = false;
        //Show
        infoView.setVisibility(View.VISIBLE);
        infoView.animate()
                .translationY(CommonVariables.VIEW_FIRSTSTATE -200)
                .setDuration(CommonVariables.ANIMATION_DURATION)
                .setListener(null);
    }

    public void hideInfoView()
    {
        infoViewFullShowed = false;
        infoView.animate()
                .translationY(CommonVariables.VIEW_FIRSTSTATE)
                .setDuration(CommonVariables.ANIMATION_DURATION)
                .setListener(null);
    }

    public boolean isInfoViewFullShowed() {
        return infoViewFullShowed;
    }

    public void setInfoViewFullShowed(boolean infoViewFullShowed) {
        this.infoViewFullShowed = infoViewFullShowed;
    }
}

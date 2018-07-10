package com.ozturkburak.mapconquer;


import android.content.Context;
import android.graphics.drawable.PictureDrawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.ozturkburak.mapconquer.model.restcountries.RestCountriesResponse;
import com.ozturkburak.mapconquer.net.ApiUtils;
import com.ozturkburak.mapconquer.utils.svg.SvgSoftwareLayerSetter;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class BorderAdapter extends RecyclerView.Adapter<BorderAdapter.ViewHolder> {

    private Context context;
    private List<String> mCountriesCode;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private RequestBuilder<PictureDrawable> mBuilder;

    BorderAdapter(Context context, List<String> countries) {
        this.mInflater = LayoutInflater.from(context);
        this.mCountriesCode = countries;
        this.context = context;

        mBuilder = Glide.with(context)
                .as(PictureDrawable.class)
                .transition(withCrossFade())
                .listener(new SvgSoftwareLayerSetter());
    }

    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = mInflater.inflate(R.layout.border_country, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the view and textview in each row
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position)
    {
        final String selectedCountry = mCountriesCode.get(position);

        Call<RestCountriesResponse> restCountriesService = ApiUtils.restCountriesService().getCountryInfo(selectedCountry);
        restCountriesService.enqueue(new Callback<RestCountriesResponse>() {
            @Override
            public void onResponse(Call<RestCountriesResponse> call, Response<RestCountriesResponse> response) {
                if (response.body() != null)
                {
                    RestCountriesResponse countriesResponse = response.body();
                    holder.countryNameTextview.setText(countriesResponse.getName());
                    mBuilder.load(countriesResponse.getFlag()).into(holder.countryFlagImage);
                }

            }

            @Override
            public void onFailure(Call<RestCountriesResponse> call, Throwable t) {
                Toast.makeText(context , t.getLocalizedMessage(),Toast.LENGTH_LONG).show();
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mCountriesCode.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView countryFlagImage;
        TextView countryNameTextview;

        ViewHolder(View itemView)
        {
            super(itemView);
            countryFlagImage = itemView.findViewById(R.id.border_item_imageview_flag);
            countryNameTextview = itemView.findViewById(R.id.border_item_textview_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public String getItem(int id) {
        return mCountriesCode.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
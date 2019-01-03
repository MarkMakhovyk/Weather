package com.mydev.android.myweather.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mydev.android.myweather.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ForecastHolder extends RecyclerView.ViewHolder {

    @BindView(R.id.icon)
    ImageView icon;

    @BindView(R.id.main)
    TextView main;

    @BindView(R.id.description)
    TextView description;

    public ForecastHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

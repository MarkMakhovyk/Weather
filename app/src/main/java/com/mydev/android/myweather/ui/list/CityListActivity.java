package com.mydev.android.myweather.ui.list;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mydev.android.myweather.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class CityListActivity extends AppCompatActivity {

    @BindView(R.id.add_city)
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.rootView, new ListUserFragment()).commit();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fab.hide();
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.rootView, new AddCityFragment()).commit();
            }
        });

    }


}



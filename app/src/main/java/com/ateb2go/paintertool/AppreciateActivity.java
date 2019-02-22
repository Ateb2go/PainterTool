package com.ateb2go.paintertool;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

public class AppreciateActivity extends AppCompatActivity {

    ViewPager vp;
    AppreciateAdapter adapter;

    Toolbar toolbar;

    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appreciate);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout=findViewById(R.id.tab);


        vp=findViewById(R.id.viewpager);
        adapter=new AppreciateAdapter(getSupportFragmentManager());
        vp.setAdapter(adapter);
        tabLayout.setupWithViewPager(vp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

package com.example.android.bakingtime.Steps;

import android.content.res.Configuration;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.bakingtime.Description.DetailedList;
import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;

public class FullDescription extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_description);
        if(getSupportActionBar()!=null) {
            getSupportActionBar().setTitle(MainActivity.dishNames[DetailedList.id]);
        }

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
            getSupportActionBar().hide();
        }else{
            getSupportActionBar().show();
        }

        FullDescriptionFragment fullDescriptionFragment=new FullDescriptionFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.main_desc, fullDescriptionFragment)
                .commit();
    }
}

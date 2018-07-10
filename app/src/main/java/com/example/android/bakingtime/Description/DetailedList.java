package com.example.android.bakingtime.Description;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.services.ChangeTitleService;
import com.example.android.bakingtime.widget.WidgetList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailedList extends AppCompatActivity {

    @BindView(R.id.fab)
    FloatingActionButton floatingActionButton;

    public static int id;
    private static final String SELECTED_FRAGMENT_STATE = "Selects";
    private SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_list);
        id = getIntent().getExtras().getInt("id");
        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            sharedPreferences = getApplicationContext().getSharedPreferences("ingrad_pref", 0);
            final SharedPreferences.Editor editor = sharedPreferences.edit();

            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(MainActivity.dishNames[id]);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            if (id == sharedPreferences.getInt("selection", 4)) {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DetailedList.this, R.drawable.ic_favorite_white_24px));
            } else {
                floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DetailedList.this, R.drawable.ic_favorite_border_white_24px));
            }

            if (MainActivity.tabletSize) {
                IngredFragmentContent contents_1 = new IngredFragmentContent();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.container_1, contents_1)
                        .commit();
                StepFragmentContent contents_2 = new StepFragmentContent();
                fragmentManager.beginTransaction()
                        .add(R.id.container_2, contents_2)
                        .commit();

            } else {

                IngredFragmentContent contents_1 = new IngredFragmentContent();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.container_1, contents_1)
                        .commit();
                StepFragmentContent contents_2 = new StepFragmentContent();
                fragmentManager.beginTransaction()
                        .add(R.id.container_2, contents_2)
                        .commit();
            }

            floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!(id == sharedPreferences.getInt("selection", 4))) {
                        editor.putInt("selection", id);
                        editor.putString("dishName", MainActivity.dishNames[id]);
                        editor.commit();
                        ChangeTitleService.startChanging(DetailedList.this);
                        WidgetList.sendRefreshBroadcast(DetailedList.this);
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DetailedList.this, R.drawable.ic_favorite_white_24px));
                    } else {
                        editor.putInt("selection", 4);
                        editor.putString("dishName", "NO ITEMS TO SHOW");
                        editor.commit();
                        ChangeTitleService.startChanging(DetailedList.this);
                        WidgetList.sendRefreshBroadcast(DetailedList.this);
                        floatingActionButton.setImageDrawable(ContextCompat.getDrawable(DetailedList.this, R.drawable.ic_favorite_border_white_24px));
                    }
                }
            });
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SELECTED_FRAGMENT_STATE, true);
    }
}

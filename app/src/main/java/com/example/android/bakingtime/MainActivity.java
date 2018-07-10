package com.example.android.bakingtime;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.Snackbar;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.android.bakingtime.Description.DetailedList;
import com.example.android.bakingtime.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingtime.Utilities.NetworkUtils;
import com.example.android.bakingtime.provider.ContractClass;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.gridviewRecycler) RecyclerView recyclerView;

    public static boolean tabletSize=false;
    public static int[] ing_numbers;
    public static int[] step_numbers;
    public static String [] dishNames;
    public static String [][] quantity;
    public static String [][] measure;
    public static String [][] ingredient;
    public static String [][] step_id;
    public static String [][] shortDescription;
    public static String [][] description;
    public static String [][] videoURL;
    public static String [][] thumbnailURL;
    public static String [] servings;
    public static String [] dishImage;
    private URL url;
    private static int NUM_COLUMNS;

    @Nullable
    private SimpleIdlingResource mIdlingResource;
    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE || tabletSize)
            NUM_COLUMNS=2;
        if(tabletSize && getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
            NUM_COLUMNS=3;
        if(!tabletSize && getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
            NUM_COLUMNS=1;
        if(isNetworkAvailable()) {
            try {
                url = new URL("https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json");
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "URL not Recognized..", Toast.LENGTH_LONG).show();
            }
            new GetRecipies().execute(url);
        }else{
            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(MainActivity.this,"No Network Available..",Toast.LENGTH_LONG).show();
        }
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public class GetRecipies extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String Results = null;
            try{
                Results= NetworkUtils.getResponseFromHttpUrl(searchUrl);
                try {
                    final JSONArray jsonArray = new JSONArray(Results);
                    dishNames=new String[jsonArray.length()];
                    servings=new String[jsonArray.length()];
                    dishImage=new String[jsonArray.length()];
                    ing_numbers=new int[jsonArray.length()];
                    step_numbers=new int[jsonArray.length()];
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo=jsonArray.getJSONObject(i);
                        dishNames[i]=jo.getString("name");
                        servings[i]=jo.getString("servings");
                        dishImage[i]=jo.getString("image");
                        JSONArray jsonArray1=jo.getJSONArray("ingredients");
                        ing_numbers[i]=jsonArray1.length();
                        JSONArray jsonArray2=jo.getJSONArray("steps");
                        step_numbers[i]=jsonArray2.length();
                    }
                    int max=ing_numbers[0];
                    for(int i = 0; i < ing_numbers.length; i++)
                        if(max < ing_numbers[i])
                            max = ing_numbers[i];
                    quantity=new String[jsonArray.length()][max];
                    measure=new String[jsonArray.length()][max];
                    ingredient=new String[jsonArray.length()][max];
                    max=step_numbers[0];
                    for(int i = 0; i < step_numbers.length; i++)
                        if(max < step_numbers[i])
                            max = step_numbers[i];
                    step_id=new String[jsonArray.length()][max];
                    shortDescription=new String[jsonArray.length()][max];
                    description=new String[jsonArray.length()][max];
                    videoURL=new String[jsonArray.length()][max];
                    thumbnailURL=new String[jsonArray.length()][max];
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jo=jsonArray.getJSONObject(i);
                        JSONArray jsonArray1=jo.getJSONArray("ingredients");
                        for(int j=0;j<jsonArray1.length();j++){
                            JSONObject je=jsonArray1.getJSONObject(j);
                            quantity[i][j]=je.getString("quantity");
                            measure[i][j]=je.getString("measure");
                            ingredient[i][j]=je.getString("ingredient");
                        }
                        JSONArray jsonArray2=jo.getJSONArray("steps");
                        for (int j=0;j<jsonArray2.length();j++){
                            JSONObject jw=jsonArray2.getJSONObject(j);
                            step_id[i][j]=jw.getString("id");
                            shortDescription[i][j]=jw.getString("shortDescription");
                            description[i][j]=jw.getString("description");
                            videoURL[i][j]=jw.getString("videoURL");
                            thumbnailURL[i][j]=jw.getString("thumbnailURL");
                        }
                    }
                }catch (JSONException e){
                    Log.d("Message","JSON Parsing Exception");
                }
            }catch (IOException e){
                Log.d("Message","IO Network Exception");
            }
            return Results;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            HomeAdapter homeAdapter =
                    new HomeAdapter(MainActivity.this);
            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(NUM_COLUMNS, LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(staggeredGridLayoutManager);
            recyclerView.setAdapter(homeAdapter);
            progressBar.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
}

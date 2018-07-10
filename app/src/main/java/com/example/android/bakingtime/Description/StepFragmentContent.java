package com.example.android.bakingtime.Description;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.bakingtime.MainActivity;
import com.example.android.bakingtime.R;
import com.example.android.bakingtime.Steps.FullDescription;
import com.example.android.bakingtime.Steps.FullDescriptionFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;



public class StepFragmentContent extends Fragment
        implements StepsAdapter.ListItemClickListener{

    @BindView(R.id.rv_ingred) RecyclerView IngredList;
    private Unbinder unbinder;

    private static final String TAG = StepFragmentContent.class.getSimpleName();

    public static int currentSelection;
    public static int ultimateFlag;

    private StepsAdapter mAdapter;
    private int numberOfClicks;

    public StepFragmentContent() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.ingredient_item, container, false);
        unbinder= ButterKnife.bind(this,rootView);

        numberOfClicks=0;
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        IngredList.setLayoutManager(layoutManager);

        IngredList.setHasFixedSize(true);

        int flag=0;
        for(int i = 0; i< MainActivity.shortDescription[DetailedList.id].length; i++){
            if(MainActivity.shortDescription[DetailedList.id][i]==null){
                break;
            }else{
                flag+=1;
                ultimateFlag=flag;
            }
        }

        mAdapter = new StepsAdapter(DetailedList.id,flag,this);
        IngredList.setAdapter(mAdapter);

        return rootView;
    }

    @Override
    public void onListItemClick(int clickedItemIndex) {
        numberOfClicks=1;
        currentSelection=clickedItemIndex;
        if(MainActivity.tabletSize){
            FullDescriptionFragment.tempSelection=clickedItemIndex;
            FullDescriptionFragment.tempoFlag=ultimateFlag;
            FullDescriptionFragment.playbackPosition=0;
            FullDescriptionFragment fullDescriptionFragment=new FullDescriptionFragment();
            FragmentManager fragmentManager = getFragmentManager();
            if(numberOfClicks==0) {
                fragmentManager.beginTransaction()
                        .add(R.id.main_desc, fullDescriptionFragment)
                        .commit();
            }else{
                fragmentManager.beginTransaction()
                        .replace(R.id.main_desc, fullDescriptionFragment)
                        .commit();
            }
        }else {
            Intent i = new Intent(getActivity(), FullDescription.class);
            FullDescriptionFragment.tempSelection=clickedItemIndex;
            FullDescriptionFragment.tempoFlag=ultimateFlag;
            startActivity(i);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
